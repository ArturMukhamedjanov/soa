#!/bin/bash

echo "🚀 Starting HAProxy..."

# Количество инстансов vehicle-service (по умолчанию 3)
VEHICLE_INSTANCES=${1:-3}

HAPROXY_CONFIG="./configs/haproxy.cfg"
HAPROXY_TEMP_CONFIG="./configs/haproxy_temp.cfg"
CERT_SOURCE="./certificates/vehicle-service.keystore"
CERT_DEST="/etc/haproxy/cert.pem"

# Проверяем установлен ли HAProxy
if ! command -v haproxy &> /dev/null; then
    echo "❌ HAProxy not found! Installing..."
    # Ubuntu/Debian
    sudo apt-get update && sudo apt-get install -y haproxy
    # CentOS/RHEL
    # sudo yum install -y haproxy
fi

# Конвертируем сертификат для HAProxy
echo "🔐 Converting certificate for HAProxy..."
if [ -f "$CERT_SOURCE" ]; then
    # Создаем временный PKCS12
    keytool -importkeystore \
        -srckeystore "$CERT_SOURCE" \
        -destkeystore /tmp/vehicle-service.p12 \
        -deststoretype PKCS12 \
        -srcstorepass password \
        -deststorepass password \
        -noprompt
    
    # Конвертируем в PEM
    openssl pkcs12 -in /tmp/vehicle-service.p12 -nodes -out /tmp/haproxy.pem -passin pass:password
    
    # Копируем в нужную директорию
    sudo mkdir -p /etc/haproxy
    sudo cp /tmp/haproxy.pem "$CERT_DEST"
    sudo chmod 644 "$CERT_DEST"
    
    # Очищаем временные файлы
    rm -f /tmp/vehicle-service.p12
    
    echo "✅ Certificate prepared for HAProxy"
else
    echo "❌ Certificate not found: $CERT_SOURCE"
    exit 1
fi

# Создаем временный конфиг с динамическим количеством инстансов
echo "📝 Generating HAProxy config for $VEHICLE_INSTANCES vehicle service instances..."

# Базовый конфиг
cat > "$HAPROXY_TEMP_CONFIG" << 'EOF'
global
    daemon
    maxconn 4096
    log 127.0.0.1 local0 info

defaults
    mode http
    timeout connect 5000ms
    timeout client 50000ms
    timeout server 50000ms
    option forwardfor
    option httplog
    log global

# HTTPS фронтенд для vehicle-service
frontend https_vehicle_frontend
    bind *:8445 ssl crt /etc/haproxy/cert.pem
    default_backend vehicle_servers

# HTTPS фронтенд для shop-service
frontend https_shop_frontend
    bind *:8446 ssl crt /etc/haproxy/cert.pem
    default_backend shop_servers

# HTTP фронтенд
frontend http_frontend
    bind *:8081
    redirect scheme https code 301 if !{ ssl_fc }

# Бэкенд для Spring сервисов (vehicle-service)
backend vehicle_servers
    balance roundrobin
    option httpchk GET /actuator/health
EOF

# Динамически добавляем серверы vehicle-service
for i in $(seq 1 $VEHICLE_INSTANCES); do
    PORT=$((25410 + i))
    echo "    server spring_instance$i 127.0.0.1:$PORT check ssl verify none inter 1000 rise 2 fall 3" >> "$HAPROXY_TEMP_CONFIG"
done

# Добавляем фиксированную часть для shop-service
cat >> "$HAPROXY_TEMP_CONFIG" << 'EOF'

# Бэкенд для WildFly сервисов (shop-service)
backend shop_servers
    balance roundrobin
    option httpchk GET /shop-service/actuator/health
    server wildfly_instance1 127.0.0.1:25402 check ssl verify none inter 1000 rise 2 fall 3
    server wildfly_instance2 127.0.0.1:25403 check ssl verify none inter 1000 rise 2 fall 3

# Статистика HAProxy
listen stats
    bind *:1936
    stats enable
    stats uri /
    stats hide-version
    stats auth admin:password
    stats refresh 10s
EOF

echo "✅ HAProxy config generated with $VEHICLE_INSTANCES vehicle service instances"

# Проверяем синтаксис конфига
if ! sudo haproxy -c -f "$HAPROXY_TEMP_CONFIG"; then
    echo "❌ HAProxy config syntax error!"
    echo "📋 Checking config file..."
    cat -n "$HAPROXY_TEMP_CONFIG" | tail -10
    exit 1
fi

# Останавливаем если уже запущен
sudo pkill haproxy || true
sleep 2

# Запускаем HAProxy с временным конфигом
sudo haproxy -f "$HAPROXY_CONFIG" -D

echo "✅ HAProxy started successfully!"
echo "🔗 HTTPS endpoint: https://localhost:8445"
echo "🔗 HTTP endpoint: http://localhost:8081 (redirects to HTTPS)"
echo "📊 Stats: http://localhost:1936 (admin:password)"
echo "🚗 Vehicle instances: $VEHICLE_INSTANCES"
echo "🛍️  Shop instances: 2"
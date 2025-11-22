#!/bin/bash

echo "🚀 Starting HAProxy..."

HAPROXY_CONFIG="./configs/haproxy.cfg"
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

# Проверяем конфиг
if [ ! -f "$HAPROXY_CONFIG" ]; then
    echo "❌ HAProxy config not found: $HAPROXY_CONFIG"
    exit 1
fi

# Добавляем пустую строку в конец файла если нужно
echo "" >> "$HAPROXY_CONFIG"

# Проверяем синтаксис конфига
if ! sudo haproxy -c -f "$HAPROXY_CONFIG"; then
    echo "❌ HAProxy config syntax error!"
    echo "📋 Checking config file..."
    cat -n "$HAPROXY_CONFIG" | tail -5
    exit 1
fi

# Останавливаем если уже запущен
sudo pkill haproxy || true
sleep 2

# Запускаем HAProxy
sudo haproxy -f "$HAPROXY_CONFIG" -D

echo "✅ HAProxy started successfully!"
echo "🔗 HTTPS endpoint: https://localhost:8445"
echo "🔗 HTTP endpoint: http://localhost:8080 (redirects to HTTPS)"
echo "📊 Stats: http://localhost:1936 (admin:password)"
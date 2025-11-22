#!/bin/bash

echo "🚀 Starting Consul..."

CONSUL_CONFIG="./configs/consul.json"
CONSUL_LOG_FILE="./logs/consul.log"

# Проверяем установлен ли Consul
if ! command -v consul &> /dev/null; then
    echo "❌ Consul not found! Please install Consul first."
    echo "📥 Download from: https://www.consul.io/downloads"
    exit 1
fi

# Проверяем существует ли конфиг
if [ ! -f "$CONSUL_CONFIG" ]; then
    echo "❌ Consul config not found: $CONSUL_CONFIG"
    exit 1
fi

# Создаем директории если нужно
mkdir -p ./logs

# Останавливаем если уже запущен
pkill -f "consul agent" || true
sleep 2

echo "📝 Consul logs will be written to: $CONSUL_LOG_FILE"

# Запускаем Consul с логированием в файл
consul agent -config-file="$CONSUL_CONFIG" -bind=127.0.0.1 -advertise=127.0.0.1 > "$CONSUL_LOG_FILE" 2>&1 &

# Ждем запуска
sleep 5

# Проверяем статус
if consul members 2>/dev/null; then
    echo "✅ Consul started successfully!"
    echo "📊 Consul UI: http://localhost:8500"
    echo "📋 Consul logs: $CONSUL_LOG_FILE"
else
    echo "❌ Failed to start Consul, checking logs..."
    tail -20 "$CONSUL_LOG_FILE"
    exit 1
fi
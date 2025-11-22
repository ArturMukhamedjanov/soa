#!/bin/bash

echo "🔍 Checking Consul status..."

# Проверяем доступность Consul
if ! curl -s http://localhost:8500/v1/agent/self > /dev/null; then
    echo "❌ Consul is not running!"
    echo "💡 Start Consul with: ./scripts/start-consul.sh"
    exit 1
fi

echo "✅ Consul is running"

# Получаем информацию о ноде
NODE_NAME=$(curl -s http://localhost:8500/v1/agent/self | jq -r '.Config.NodeName')
DATACENTER=$(curl -s http://localhost:8500/v1/agent/self | jq -r '.Config.Datacenter')

echo "📊 Consul Node: $NODE_NAME"
echo "🏢 Datacenter: $DATACENTER"

# Проверяем сервисы
SERVICES=$(curl -s http://localhost:8500/v1/catalog/services)
SERVICE_COUNT=$(echo "$SERVICES" | jq 'length')

echo "📋 Registered services: $SERVICE_COUNT"
echo "$SERVICES" | jq .

# Проверяем health checks
HEALTH_CHECKS=$(curl -s http://localhost:8500/v1/health/state/any)
CHECK_COUNT=$(echo "$HEALTH_CHECKS" | jq 'length')

echo "🏥 Health checks: $CHECK_COUNT"
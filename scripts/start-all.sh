#!/bin/bash

echo "🚀 Starting SOA Infrastructure..."
echo "=========================================="

# Создаем директории
mkdir -p logs pids

# Функция для проверки порта
check_port() {
    nc -z localhost "$1" >/dev/null 2>&1
}

# Запускаем Consul
echo "1. Starting Consul..."
./scripts/start-consul.sh
sleep 5

# Запускаем HAProxy
echo ""
echo "2. Starting HAProxy..."
./scripts/start-haproxy.sh
sleep 3

# Запускаем инстансы сервиса
echo ""
echo "3. Starting Vehicle Service Instances..."

INSTANCES=3
for i in $(seq 1 $INSTANCES); do
    echo "   Starting instance $i..."
    ./scripts/start-service.sh "$i"
    sleep 2
done

echo ""
echo "=========================================="
echo "🎉 SOA Infrastructure Started Successfully!"
echo ""
echo "📊 Consul UI:      http://localhost:8500"
echo "🔗 HAProxy Stats:  http://localhost:1936 (admin:password)"
echo "🔐 HTTPS Gateway:  https://localhost:8445"
echo "🔄 HTTP Gateway:   http://localhost:8080"
echo ""
echo "🚗 Vehicle Service Instances:"
echo "   • Instance 1: https://localhost:25401"
echo "   • Instance 2: https://localhost:25402" 
echo "   • Instance 3: https://localhost:25403"
echo ""
echo "🔍 Health Checks:"
echo "   curl -k https://localhost:8445/actuator/health"
echo "   curl http://localhost:8500/v1/health/service/vehicle-service"
echo ""
echo "⏹️  To stop all services: ./scripts/stop-all.sh"
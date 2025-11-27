#!/bin/bash

echo "🚀 Starting SOA Infrastructure..."
echo "=========================================="

# Параметр для количества инстансов vehicle-service
VEHICLE_INSTANCES=${1:-3}

# Проверяем что параметр число
if ! [[ "$VEHICLE_INSTANCES" =~ ^[0-9]+$ ]] || [ "$VEHICLE_INSTANCES" -lt 1 ]; then
    echo "❌ Error: Number of instances must be a positive integer"
    echo "💡 Usage: $0 [number_of_vehicle_instances]"
    echo "   Default: 3 instances"
    exit 1
fi

echo "📊 Vehicle Service Instances: $VEHICLE_INSTANCES"

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

# Запускаем HAProxy с указанием количества инстансов
echo ""
echo "2. Starting HAProxy..."
./scripts/start-haproxy.sh "$VEHICLE_INSTANCES"
sleep 3

# Запускаем инстансы сервиса
echo ""
echo "3. Starting Vehicle Service Instances..."

for i in $(seq 1 $VEHICLE_INSTANCES); do
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
echo "🔄 HTTP Gateway:   http://localhost:8081"
echo ""
echo "🚗 Vehicle Service Instances: $VEHICLE_INSTANCES"

# Динамически показываем порты для vehicle-service
for i in $(seq 1 $VEHICLE_INSTANCES); do
    PORT=$((25410 + i))
    echo "   • Instance $i: https://localhost:$PORT"
done

echo ""
echo "🔍 Health Checks:"
echo "   curl -k https://localhost:8445/actuator/health"
echo "   curl http://localhost:8500/v1/health/service/vehicle-service"
echo ""
echo "⏹️  To stop all services: ./scripts/stop-all.sh"
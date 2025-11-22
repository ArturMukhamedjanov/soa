#!/bin/bash

# Параметры
INSTANCE_NUMBER=${1:-1}
PORT=$((25400 + INSTANCE_NUMBER))
LOG_FILE="./logs/vehicle-service-$INSTANCE_NUMBER.log"
PID_FILE="./pids/vehicle-service-$INSTANCE_NUMBER.pid"

echo "🚀 Starting Vehicle Service Instance $INSTANCE_NUMBER on port $PORT..."

# Создаем директории если нужно
mkdir -p ./logs ./pids

# Проверяем есть ли JAR файл
if [ ! -f "./vehicle-service/target/vihicle-service-0.0.1-SNAPSHOT.jar" ]; then
    echo "❌ JAR file not found: ./vehicle-service/target/vihicle-service-0.0.1-SNAPSHOT.jar"
    exit 1
fi

# Останавливаем если уже запущен
if [ -f "$PID_FILE" ]; then
    OLD_PID=$(cat "$PID_FILE")
    if kill -0 "$OLD_PID" 2>/dev/null; then
        echo "🛑 Stopping existing instance $INSTANCE_NUMBER (PID: $OLD_PID)"
        kill "$OLD_PID"
        sleep 3
    fi
    rm -f "$PID_FILE"
fi

# Запускаем сервис с переопределением ТОЛЬКО нужных параметров
nohup java -jar ./vehicle-service/target/vihicle-service-0.0.1-SNAPSHOT.jar \
    --server.port=$PORT \
    --spring.cloud.consul.discovery.instance-id=vehicle-service-instance-$INSTANCE_NUMBER \
    > "$LOG_FILE" 2>&1 &

# Сохраняем PID
echo $! > "$PID_FILE"

echo "✅ Vehicle Service Instance $INSTANCE_NUMBER started (PID: $!, Port: $PORT)"
echo "📋 Log file: $LOG_FILE"
echo "🔍 Health check: curl -k https://localhost:$PORT/actuator/health"

# Ждем немного и проверяем запуск
sleep 10  # Увеличиваем время ожидания для Spring Boot + Consul
if curl -k "https://localhost:$PORT/actuator/health" >/dev/null 2>&1; then
    echo "✅ Instance $INSTANCE_NUMBER is healthy"
else
    echo "⚠️  Instance $INSTANCE_NUMBER health check failed, check logs: $LOG_FILE"
    echo "📝 Last log entries:"
    tail -10 "$LOG_FILE"
fi
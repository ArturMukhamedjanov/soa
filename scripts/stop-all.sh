#!/bin/bash

echo "🛑 Stopping SOA Infrastructure..."
echo "=========================================="

echo "1. Stopping Vehicle Service Instances..."
for pid_file in ./pids/vehicle-service-*.pid; do
    if [ -f "$pid_file" ]; then
        pid=$(cat "$pid_file")
        instance=$(echo "$pid_file" | grep -o '[0-9]\+')
        echo "   Stopping instance $instance (PID: $pid)"
        kill "$pid" 2>/dev/null || true
        rm -f "$pid_file"
    fi
done

for pid_file in ./pids/consul-*.pid; do
    if [ -f "$pid_file" ]; then
        pid=$(cat "$pid_file")
        instance=$(echo "$pid_file" | grep -o '[0-9]\+')
        echo "   Stopping instance $instance (PID: $pid)"
        kill "$pid" 2>/dev/null || true
        rm -f "$pid_file"
    fi
done


echo ""
echo "2. Stopping HAProxy..."
sudo pkill haproxy || true
sleep 2

echo ""
echo "3. Stopping Consul..."
pkill -f "consul agent" || true
sleep 2

echo ""
echo "✅ All services stopped!"
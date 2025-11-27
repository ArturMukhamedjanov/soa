#!/bin/bash

echo "🚀 Starting HAProxy with Native Consul Integration..."

VEHICLE_INSTANCES=${1:-3}

HAPROXY_CONFIG="./configs/haproxy-consul.cfg"
HAPROXY_PID_FILE="./pids/haproxy.pid"
CERT_SOURCE="./certificates/vehicle-service.keystore"
CERT_DEST="/etc/haproxy/cert.pem"

if ! command -v haproxy &> /dev/null; then
    echo "❌ HAProxy not found! Installing..."
    sudo apt-get update && sudo apt-get install -y haproxy
fi

echo "🔍 Checking Consul DNS..."
if ! dig @127.0.0.1 -p 8600 -t srv _vehicle-service._tcp.service.consul +short &>/dev/null; then
    echo "⚠️  Consul DNS not responding, but continuing..."
fi

echo "🔐 Preparing certificate..."
if [ -f "$CERT_SOURCE" ]; then
    keytool -importkeystore \
        -srckeystore "$CERT_SOURCE" \
        -destkeystore /tmp/vehicle-service.p12 \
        -deststoretype PKCS12 \
        -srcstorepass password \
        -deststorepass password \
        -noprompt
    
    openssl pkcs12 -in /tmp/vehicle-service.p12 -nodes -out /tmp/haproxy.pem -passin pass:password
    
    sudo mkdir -p /etc/haproxy
    sudo cp /tmp/haproxy.pem "$CERT_DEST"
    sudo chmod 644 "$CERT_DEST"
    
    rm -f /tmp/vehicle-service.p12 /tmp/haproxy.pem
    
    echo "✅ Certificate prepared for HAProxy"
else
    echo "❌ Certificate not found: $CERT_SOURCE"
    exit 1
fi

echo "🔍 Checking HAProxy config syntax..."
if ! sudo haproxy -c -f "$HAPROXY_CONFIG"; then
    echo "❌ HAProxy config syntax error!"
    exit 1
fi

echo "🛑 Stopping existing HAProxy..."
sudo pkill haproxy || true
sleep 2

echo "📝 Starting HAProxy with native Consul integration..."
sudo haproxy -f "$HAPROXY_CONFIG" -p "$HAPROXY_PID_FILE " -d > ./logs/haproxy-debug.log 2>&1 &

if [ $? -eq 0 ]; then
    echo "✅ HAProxy started successfully with native Consul integration"
else
    echo "❌ Failed to start HAProxy"
    exit 1
fi

echo ""
echo "🎉 HAProxy with Native Consul Integration Started Successfully!"
echo ""
echo "🔗 Vehicle Service (dynamic): https://localhost:8445"
echo "   - Auto-discovering via: _vehicle-service._tcp.service.consul"
echo "🔗 Shop Service (static):     https://localhost:8446"
echo "📊 Stats:                     http://localhost:1936 (admin:password)"
echo ""
echo "🔍 To check service discovery:"
echo "   dig @127.0.0.1 -p 8600 -t srv _vehicle-service._tcp.service.consul +short"
echo ""
echo "⚡ HAProxy will automatically discover and update vehicle services every 10 seconds"





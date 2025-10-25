# Payara HTTPS Configuration Instructions

This document contains instructions for configuring Payara to enable HTTPS with a self-signed certificate for the Shop Service.

## Step 1: Generate a Self-Signed Certificate

```bash
# Create a directory for the keystore
mkdir -p $PAYARA_HOME/glassfish/domains/domain1/config/certificates

# Generate a keystore with a self-signed certificate
keytool -genkeypair -alias shop-service -keyalg RSA -keysize 2048 -validity 365 \
        -keystore $PAYARA_HOME/glassfish/domains/domain1/config/certificates/shop-service.keystore \
        -storepass password -keypass password \
        -dname "CN=localhost, OU=SOA, O=University, L=City, ST=State, C=RU"
```

## Step 2: Configure Payara for HTTPS

You can configure HTTPS in Payara using either the admin console or the command line:

### Option 1: Using Admin Console

1. Start Payara Server
```bash
$PAYARA_HOME/bin/asadmin start-domain
```

2. Access the Admin Console at `http://localhost:4848`

3. Navigate to Configurations → server-config → HTTP Service → HTTP Listeners

4. Select the `http-listener-2` (secure listener) and configure:
   - Set Enabled to true
   - Set Security to true
   - Set Keystore File to `${com.sun.aas.instanceRoot}/config/certificates/shop-service.keystore`
   - Set Keystore Password to `password`
   - Set Certificate Nickname to `shop-service`
   - Save the changes

5. To disable HTTP access, you can either:
   - Set Enabled to false for http-listener-1, or
   - Configure redirect from HTTP to HTTPS

### Option 2: Using Command Line

```bash
# Start Payara if not already running
$PAYARA_HOME/bin/asadmin start-domain

# Configure the secure HTTP listener
$PAYARA_HOME/bin/asadmin set configs.config.server-config.network-config.protocols.protocol.sec-admin-listener.ssl.cert-nickname=shop-service
$PAYARA_HOME/bin/asadmin set configs.config.server-config.network-config.protocols.protocol.sec-admin-listener.ssl.key-store=${com.sun.aas.instanceRoot}/config/certificates/shop-service.keystore
$PAYARA_HOME/bin/asadmin set configs.config.server-config.network-config.protocols.protocol.sec-admin-listener.ssl.key-store-password=password

# Configure HTTP listener to redirect to HTTPS
$PAYARA_HOME/bin/asadmin set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.redirect-port=8181

# Restart Payara to apply changes
$PAYARA_HOME/bin/asadmin restart-domain
```

## Step 3: Deploy the Application

```bash
# Build the application
mvn clean package

# Deploy to Payara
$PAYARA_HOME/bin/asadmin deploy --contextroot /shop target/shop-service.war
```

## Step 4: Configure Client Certificate for Communication with Vehicle Service

Since the Vehicle Service is also using HTTPS with a self-signed certificate, we need to add its certificate to Payara's truststore:

```bash
# Export the certificate from WildFly's keystore
keytool -export -alias vehicle-service \
        -file vehicle-service-cert.crt \
        -keystore $WILDFLY_HOME/standalone/configuration/certificates/vehicle-service.keystore \
        -storepass password

# Import the certificate into Payara's cacerts truststore
keytool -import -trustcacerts -alias vehicle-service \
        -file vehicle-service-cert.crt \
        -keystore $JAVA_HOME/lib/security/cacerts \
        -storepass changeit -noprompt
```

## Step 5: Test HTTPS Configuration

Access the service at:
```
https://localhost:8181/shop
```

Note: Since we're using a self-signed certificate, your browser will show a security warning. You'll need to accept the risk to proceed.
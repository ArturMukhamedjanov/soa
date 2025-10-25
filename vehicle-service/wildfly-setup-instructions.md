# WildFly HTTPS Configuration Instructions

This document contains instructions for configuring WildFly to enable HTTPS with a self-signed certificate for the Vehicle Service.

## Step 1: Generate a Self-Signed Certificate

```bash
# Create a directory for the keystore
mkdir -p $WILDFLY_HOME/standalone/configuration/certificates

# Generate a keystore with a self-signed certificate
keytool -genkeypair -alias vehicle-service -keyalg RSA -keysize 2048 -validity 365 \
        -keystore $WILDFLY_HOME/standalone/configuration/certificates/vehicle-service.keystore \
        -storepass password -keypass password \
        -dname "CN=localhost, OU=SOA, O=University, L=City, ST=State, C=RU"
```

## Step 2: Configure WildFly for HTTPS

Add the following configuration to your WildFly `standalone.xml` file within the `security-realms` section:

```xml
<security-realm name="SSLRealm">
    <server-identities>
        <ssl>
            <keystore path="certificates/vehicle-service.keystore" relative-to="jboss.server.config.dir" keystore-password="password" alias="vehicle-service" key-password="password"/>
        </ssl>
    </server-identities>
</security-realm>
```

Then, find the `<subsystem xmlns="urn:jboss:domain:undertow:X.Y">` section and add/modify the HTTPS listener:

```xml
<subsystem xmlns="urn:jboss:domain:undertow:X.Y">
    <server name="default-server">
        <https-listener name="https" socket-binding="https" security-realm="SSLRealm" enable-http2="true"/>
        <!-- ... other listeners ... -->
        <host name="default-host" alias="localhost">
            <!-- ... host configuration ... -->
        </host>
    </server>
</subsystem>
```

## Step 3: Disable HTTP Access

To disable HTTP access and force HTTPS usage only, you have two options:

### Option 1: Remove the HTTP listener
Find and remove the HTTP listener in the `standalone.xml` file:

```xml
<!-- Remove this line -->
<http-listener name="default" socket-binding="http" redirect-socket="https" enable-http2="true"/>
```

### Option 2: Configure HTTP to redirect to HTTPS
Modify the HTTP listener to redirect to HTTPS:

```xml
<http-listener name="default" socket-binding="http" redirect-socket="https" enable-http2="true" redirect-to-https="true"/>
```

## Step 4: Deploy the Application

```bash
# Build the application
mvn clean package

# Deploy to WildFly
cp target/vehicle-service.war $WILDFLY_HOME/standalone/deployments/
```

## Step 5: Test HTTPS Configuration

Access the service at:
```
https://localhost:8443/vehicles
```

Note: Since we're using a self-signed certificate, your browser will show a security warning. You'll need to accept the risk to proceed.
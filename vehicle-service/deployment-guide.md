# Vehicle Service Deployment Guide

This guide provides step-by-step instructions for deploying the Vehicle Service to WildFly with HTTPS support.

## Prerequisites

- Java 11 or later installed
- Maven 3.6 or later installed
- WildFly 38.0.0.Final installed
- PostgreSQL installed and running

## Step 1: Set Up PostgreSQL Database

1. Create the database:
```sql
CREATE DATABASE soa_db;
CREATE USER soa_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE soa_db TO soa_user;
```

## Step 2: Configure WildFly for HTTPS

1. Create a directory for the keystore:
```bash
mkdir -p $WILDFLY_HOME/standalone/configuration/certificates
```

2. Generate a keystore with a self-signed certificate:
```bash
keytool -genkeypair -alias vehicle-service -keyalg RSA -keysize 2048 -validity 365 \
        -keystore $WILDFLY_HOME/standalone/configuration/certificates/vehicle-service.keystore \
        -storepass password -keypass password \
        -dname "CN=localhost, OU=SOA, O=University, L=City, ST=State, C=RU"
```

3. Make sure the security-realm is configured in WildFly's standalone.xml:
```xml
<security-realm name="SSLRealm">
    <server-identities>
        <ssl>
            <keystore path="certificates/vehicle-service.keystore" relative-to="jboss.server.config.dir" 
                     keystore-password="password" alias="vehicle-service" key-password="password"/>
        </ssl>
    </server-identities>
</security-realm>
```

4. Ensure the HTTPS listener is configured:
```xml
<https-listener name="https" socket-binding="https" security-realm="SSLRealm" enable-http2="true"/>
```

## Step 3: Configure WildFly for PostgreSQL

1. Download the PostgreSQL JDBC Driver:
```bash
wget https://jdbc.postgresql.org/download/postgresql-42.6.0.jar -O postgresql.jar
```

2. Install the driver as a WildFly module:
```bash
$WILDFLY_HOME/bin/jboss-cli.sh --command="module add --name=org.postgresql --resources=postgresql.jar --dependencies=javax.api,javax.transaction.api"
```

3. Add the JDBC driver to the WildFly configuration:
```bash
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="/subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)"
```

4. Create a datasource:
```bash
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="data-source add --name=VehicleServiceDS --jndi-name=java:/VehicleServiceDS --driver-name=postgresql --connection-url=jdbc:postgresql://localhost:5432/soa_db --user-name=soa_user --password=password --min-pool-size=5 --max-pool-size=20"
```

## Step 4: Build and Deploy the Vehicle Service

1. Build the WAR file:
```bash
cd vehicle-service
mvn clean package
```

2. Deploy to WildFly:
```bash
cp target/vehicle-service.war $WILDFLY_HOME/standalone/deployments/
```

## Step 5: Start WildFly

1. Start WildFly with the standalone.sh/standalone.bat script:
```bash
$WILDFLY_HOME/bin/standalone.sh
# OR on Windows
$WILDFLY_HOME/bin/standalone.bat
```

## Step 6: Test the Deployment

1. Access the Vehicle Service at:
```
https://localhost:8443/vehicles
```
   - Note: Since we're using a self-signed certificate, your browser will show a security warning. Accept the risk to proceed.

## Troubleshooting

If you encounter deployment issues:

1. Check the WildFly logs:
```
$WILDFLY_HOME/standalone/log/server.log
```

2. Common issues:
   - Database connection failures: Verify PostgreSQL is running and the connection details are correct
   - Certificate issues: Ensure the keystore file exists in the specified location
   - Class not found errors: Make sure all dependencies are correctly specified in pom.xml

## Recent Fixes

The following fixes were applied to resolve deployment issues:

1. Added JSP API dependencies to pom.xml:
   - javax.servlet.jsp-api
   - javax.servlet.jsp.jstl-api

2. Fixed ErrorResponseDTO class by removing unused @AllArgsConstructor import

These changes resolved class loading errors related to JSP tag libraries used by Spring MVC.
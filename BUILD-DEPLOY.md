# AltoroJ OIDC Build and Deploy Process

This document describes the process used to build and deploy changes to the OIDC-enabled AltoroJ application running at:

    https://oidc.tuesday2.ca

---

## Environment

### Source Tree

Authoritative source code:

    /opt/altoroj-oidc/source

### Build Container

Gradle/JDK8 build environment:

    altoroj-oidc-build

Project location inside container:

    /home/gradle/project

### Runtime Container

Tomcat application container:

    altoroj-oidc

### Deployment Directory

WAR file used by the Docker build:

    /opt/altoroj-oidc/altoromutual.war

---

## Standard Deployment Procedure

### 1. Make Changes

Modify files under:

    /opt/altoroj-oidc/source

Examples:

    WebContent/js/dpop-client.js
    WebContent/dpopInit.jsp
    src/com/ibm/security/appscan/altoromutual/servlet/*.java

---

### 2. Copy Modified Files Into Build Container

Examples:

```bash
sudo docker cp \
  /opt/altoroj-oidc/source/WebContent/js/dpop-client.js \
  altoroj-oidc-build:/home/gradle/project/WebContent/js/dpop-client.js
```

```bash
sudo docker cp \
  /opt/altoroj-oidc/source/WebContent/dpopInit.jsp \
  altoroj-oidc-build:/home/gradle/project/WebContent/dpopInit.jsp
```

Java example:

```bash
sudo docker cp \
  /opt/altoroj-oidc/source/src/com/ibm/security/appscan/altoromutual/servlet/OidcCallbackServlet.java \
  altoroj-oidc-build:/home/gradle/project/src/com/ibm/security/appscan/altoromutual/servlet/OidcCallbackServlet.java
```

---

### 3. Build WAR

```bash
sudo docker exec -w /home/gradle/project \
  altoroj-oidc-build \
  gradle clean war --no-daemon
```

Successful output ends with:

```text
BUILD SUCCESSFUL
```

Generated WAR:

```text
/home/gradle/project/build/libs/altoromutual.war
```

---

### 4. Copy WAR To Deployment Directory

```bash
sudo docker cp \
  altoroj-oidc-build:/home/gradle/project/build/libs/altoromutual.war \
  /opt/altoroj-oidc/altoromutual.war
```

---

### 5. Rebuild Runtime Image

```bash
cd /opt/altoroj-oidc

sudo docker compose build altoroj-oidc
```

---

### 6. Restart AltoroJ

Only restart the application service.

Do NOT restart Keycloak unless necessary.

```bash
sudo docker compose up -d altoroj-oidc
```

---

### 7. Verify Deployment

Application:

```text
https://oidc.tuesday2.ca
```

Check running containers:

```bash
sudo docker ps
```

View logs:

```bash
sudo docker logs -f altoroj-oidc
```

---

## Current DPoP Test Page

URL:

```text
https://oidc.tuesday2.ca/dpopInit.jsp
```

Purpose:

- Generate browser DPoP keypair
- Store keypair in IndexedDB
- Generate RFC7638 JWK Thumbprint (JKT)
- Display JKT for testing

Example JKT observed during testing:

```text
E9yTPbuH5ovaLjEma_-IIHvBF2F1lK8p9xlBUw3LXwQ
```

The exact JKT depends on the browser profile, but should remain stable across refreshes.

---

## Important Notes

The build container contains the source tree used to build the WAR:

```text
/home/gradle/project
```

Changes made under:

```text
/opt/altoroj-oidc/source
```

are NOT automatically included in builds.

Modified files must be copied into the build container before running:

```bash
gradle clean war --no-daemon
```

Failure to copy updated files into the build container will result in the previous code being deployed.

---

## Important
The build container contains its own working copy: /home/gradle/project
Historically this copy contains local OIDC modifications that are not tracked by the branch checked out inside the container.
Before building, copy modified source files from: /opt/altoroj-oidc/source to: /home/gradle/project using docker cp.

Do not assume the Git branch inside the build container matches the Git branch checked out on the host.

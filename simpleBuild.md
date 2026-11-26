Modify files under:

    /opt/altoroj-oidc/source

```copy files to build container
sudo docker cp \
  /opt/altoroj-oidc/source/WebContent/js/dpop-client.js \
  altoroj-oidc-build:/home/gradle/project/WebContent/js/dpop-client.js

Java example:
sudo docker cp \
  /opt/altoroj-oidc/source/src/com/ibm/security/appscan/altoromutual/servlet/OidcCallbackServlet.java \
  altoroj-oidc-build:/home/gradle/project/src/com/ibm/security/appscan/altoromutual/servlet/OidcCallbackServlet.java

Build .war file

sudo docker exec -w /home/gradle/project \
  altoroj-oidc-build \
  gradle clean war --no-daemon

Copy .war file
sudo docker cp \
  altoroj-oidc-build:/home/gradle/project/build/libs/altoromutual.war \
  /opt/altoroj-oidc/altoromutual.war

Deploy new build
cd /opt/altoroj-oidc
sudo docker compose build altoroj-oidc

Restart AltoroJ
sudo docker compose up -d altoroj-oidc

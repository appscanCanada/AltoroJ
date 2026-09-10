FROM tomcat:9.0-jdk8

# Remove default applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR built by Gradle
COPY build/libs/altoromutual.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

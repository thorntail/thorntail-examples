FROM openjdk:jre-alpine

ADD hollow-jar/target/example-docker-jaxrs-hollow-hollow-thorntail.jar /opt/hollow-thorntail.jar
ADD application/target/example-docker-jaxrs-hollow-application.war /opt/application.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/hollow-thorntail.jar", "-Djava.net.preferIPv4Stack=true", "/opt/application.war"]

FROM openjdk:jre-alpine

ADD example-docker-jaxrs-dockerfile-thorntail.jar /opt/thorntail.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/thorntail.jar"]

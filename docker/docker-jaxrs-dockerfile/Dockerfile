FROM openjdk:jre-alpine

ADD target/example-docker-jaxrs-dockerfile-thorntail.jar /opt/thorntail.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/thorntail.jar", "-Djava.net.preferIPv4Stack=true"]

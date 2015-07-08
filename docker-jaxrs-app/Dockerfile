FROM java:openjdk-8-jdk

ADD target/DockerJaxRS-swarm.jar /opt/DockerJaxRS-swarm.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/DockerJaxRS-swarm.jar"]

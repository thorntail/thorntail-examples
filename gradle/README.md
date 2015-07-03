# Gradle plugin example

Beginning of a Gradle plugin example.

## Configuration

If you're using the default `application` plugin, The `wildfly-swarm-plugin`
can piggy-back on the `mainClassName` build property.

Else, you can set `swarm.mainClassName` to specify the main-class for you
application.


## Run

* ./gradlew clean wildfly-swarm-package && java -jar ./build/lib/wildfly-swarm-example-gradle-swarm.jar

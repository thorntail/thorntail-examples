# Gradle plugin example

Beginning of a Gradle plugin example.

*Note:* gradle doesn't honor dependency exclusions specified in poms
by default, so we're using the `dependency-management-plugin` to
enable that (see `build.gradle`). Without that, you'll get a much
larger uberjar than you should.


## Build

`./gradlew clean wildfly-swarm-package`


##Run

`java -jar ./build/libs/wildfly-swarm-example-gradle-swarm.jar`


## Configuration

If you're using the default `application` plugin, The `wildfly-swarm-plugin`
can piggy-back on the `mainClassName` build property.

Else, you can set `swarm.mainClassName` to specify the main-class for you
application.


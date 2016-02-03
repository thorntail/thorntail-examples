# Gradle + JAX-RS + EJB + MAIL example

Rest service with async EBJ method for sending mails.

*Note:* gradle doesn't honor dependency exclusions specified in poms
by default, so we're using the `dependency-management-plugin` to
enable that (see `build.gradle`). Without that, you'll get a much
larger uberjar than you should.


## Build

`./gradlew clean wildfly-swarm-package`


## Run

`java -jar ./build/libs/gradle-mail-swarm.jar`


## Configuration

By default, this will try to connect to an SMTP server at
`localhost:25`. You can override this any of three ways:

* Edit `build.gradle` and change the `smtp.*` properties, then rebuild
  the swarm jar. This will embed the properties in the jar itself.
* Pass `smtp.*` properties to gradle when building the jar:
  `./gradlew clean wildfly-swarm-package -Dsmtp.host=foo` This will
  also embed the properties in the jar itself.
* Pass properties to the application at runtime:
  `java -Dsmtp.host=foo -jar ./build/libs/gradle-mail-swarm.jar`
  This will override the property that was embedded in the jar.


# Gradle + JAX-RS + CDI + JPA Example

Rest service exposing entities loaded from a database.

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/THORN

## Build

`./gradlew clean thorntail-package`


## Run

You can run it using one of these ways:

* `java -jar ./target/example-gradle-jpa-jaxrs-cdi-thorntail.jar`
* In your IDE run the `org.wildfly.swarm.Swarm` class

## Use

* Check http://localhost:8181/ in your browser,
* or run `curl http://localhost:8181/` on your command line.


## Configuration

By default, server will run on address `localhost:8181`. You can override this by either:

* Editing following section in `build.gradle`:
  ```
  thorntail {
    properties {
      swarm.http.host = 'localhost'
      swarm.http.port = 8181
    }
  }
  ```
  Then rebuild the project.
* Passing `swarm.http.*` properties in the build command:
  ```./gradlew clean thorntail-package -Dswarm.http.port=8080```
* Pass properties to the application at runtime:
  `java -Dswarm.http.port=8080 -jar example-gradle-jpa-jaxrs-cdi-thorntail.jar`
  This will override the property that was embedded in the jar.

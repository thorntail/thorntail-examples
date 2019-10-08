# Gradle Composite Build Example

This project showcases the following capabilities,

1. Include your dependent projects / platforms via the  [Gradle composite build](https://docs.gradle.org/current/userguide/composite_builds.html) capability for your regular application development.
2. Consume dependencies that are published via the [Java platform](https://docs.gradle.org/current/userguide/java_platform_plugin.html). This is akin to consuming a BOM project (in Maven).

Instead of creating multiple projects, this module will attempt to consume one of other projects listed in the [Gradle examples](../) folder.
## Build

`./gradlew clean thorntail-package`

## Run

`java -jar ./build/libs/thorntail-composite-app-thorntail.jar`


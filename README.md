# JAX-RS and CDI .war Example

This example takes a normal JAX-RS and CDI build, and wraps it into
a `-swarm` runnable jar.

## Project `pom.xml`

This project is a traditional JAX-RS and CDI project, with maven packaging
of `war` in the `pom.xml`

    <packaging>war</packaging>

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

    <plugin>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-plugin</artifactId>
      <version>${version.wildfly-swarm}</version>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>create</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

To define the needed parts of WildFly Swarm, the following dependencies are added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-jaxrs</artifactId>
        <version>${version.wildfly-swarm}</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-weld</artifactId>
        <version>${version.wildfly-swarm}</version>
        <scope>provided</scope>
    </dependency>

This dependency provides the JAX-RS and CDI APIs to your application, so the
project does *not* need to specify those.

Additional application dependencies (in this case `lombok`) can be
specified and will be included in the normal `.war` construction and available
within the WildFly Swarm application `.jar`.

## Build

    mvn package

## Run

    java -jar ./target/wildfly-swarm-example-jaxrs-cdi-1.0.0.Beta1-SNAPSHOT-swarm.jar 

## Use

Since WildFly Swarm apps tend to support one deployment per executable, it
automatically adds a `jboss-web.xml` to the deployment if it doesn't already
exist.  This is used to bind the deployment to the root of the web-server,
instead of using the `.war`'s own name as the application context.

    http://localhost:8080/
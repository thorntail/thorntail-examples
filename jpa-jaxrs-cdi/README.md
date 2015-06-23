# JPA, JAX-RS and CDI .war Example

This example takes a normal JPA, CDI and JAX-RS build, and wraps it into
a `-swarm` runnable jar.

> Please raise any issues found with this example in this repo:
> https://github.com/wildfly-swarm/wildfly-swarm-examples
>
> Issues related to WildFly Swarm core should be raised in the main repo:
> https://github.com/wildfly-swarm/wildfly-swarm/issues

## Project `pom.xml`

This project is a normal maven project with `war` packaging.

    <packaging>war</packaging>

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

    <plugin>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-plugin</artifactId>
      <version>${version.wildfly-swarm}</version>
      <executions>
        <execution>
          <goals>
            <goal>package</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

To define the needed parts of WildFly Swarm, a few dependencies are added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-jpa</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-weld-jaxrs</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

The `wildfly-swarm-jpa` dependency provides the JPA APIs.  The `wildfly-swarm-weld-jaxrs` provides the CDI and JAX-RS
APIs.

## Run

You can run it many ways:

* mvn package && java -jar ./target/wildfly-swarm-example-jpa-jaxrs-cdi-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.Swarm` class

## Use

    http://localhost:8080/
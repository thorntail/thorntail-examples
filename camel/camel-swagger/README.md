# Camel Swagger Example

This example uses camel-swager and the Camel Rest DSL to create a JAX-RS andpoint with Swagger API docs.

## Project `pom.xml`

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

    <plugin>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-plugin</artifactId>
    </plugin>

To define the needed parts of WildFly Swarm, the following dependencies are added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>camel-undertow</artifactId>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>camel-other</artifactId>
    </dependency>

This dependency provides Camel and Swagger APIs to your application, so the
project does *not* need to specify those.

## Run

You can run it many ways:

* java -jar ./target/example-camel-mail-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.Swarm` class

## Use

Once the Swarm container has started, you can test the application by hitting the following HTTP endpoint.

    http://localhost:8080/rest/api-doc

You should see the Swagger API definition.

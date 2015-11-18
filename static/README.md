# Static Content Example

This example serves static content using WildFly Swarm.

> Please raise any issues found with this example in this repo:
> https://github.com/wildfly-swarm/wildfly-swarm-examples
>
> Issues related to WildFly Swarm core should be raised in the main repo:
> https://github.com/wildfly-swarm/wildfly-swarm/issues

## Project `pom.xml`

The project is a normal maven project with `jar` packaging, not `war`.

    <packaging>jar</packaging>

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

    <plugin>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-plugin</artifactId>
      <version>${version.wildfly-swarm}</version>
      <configuration>
        <mainClass>org.wildfly.swarm.examples.staticcontent.Main</mainClass>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>package</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

To define the needed parts of WildFly Swarm, a dependency is added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-undertow</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

## Project `main()`

The main() simply deploys static content from the classpath.

    package org.wildfly.swarm.examples.staticcontent;

    import org.jboss.shrinkwrap.api.ShrinkWrap;
    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.undertow.WARArchive;

    public class Main {

        public static void main(String[] args) throws Exception {

            Container container = new Container();

            WARArchive deployment = ShrinkWrap.create(WARArchive.class);

            deployment.staticContent();

            container.start().deploy(deployment);

        }
    }


## Run

You can run it many ways:

* mvn package && java -jar ./target/wildfly-swarm-example-static-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.staticcontent.Main` class

## Use

    http://localhost:8080/

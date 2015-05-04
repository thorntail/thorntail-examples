# JAX-RS & ShrinkWrap Example

This examples uses JAX-RS resource implementations and deploys
them through a user-provided `main()` programatically without
construction a `.war` file during the build.

## Project `pomx.xml`

The project is a normal maven project with `jar` packaging, not `war`.

    <packaging>jar</packaging>

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

Additionally, the usual `maven-jar-plugin` is provided configuration
to indicate which of our own classes should be used for the `main()`
method. 

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <configuration>
            <archive>
                <manifest>
                    <mainClass>org.wildfly.swarm.examples.jaxrs.shrinkwrap.Main</mainClass>
                </manifest>
            </archive>
        </configuration>
    </plugin>

To define the needed parts of WildFly Swarm, a dependency is added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-jaxrs</artifactId>
        <version>${version.wildfly-swarm}</version>
        <scope>provided</scope>
    </dependency>

This dependency allows usage of ShrinkWrap APIs within the `main()` in addition
to providing the JAX-RS APIs.

## Project `main()`

Since this project deploys JAX-RS resources without a `.war` being construction, it
provides its own `main()` method (specified above via the `maven-jar-plugin`) to
configure the container and deploy the resources programatically.

    package org.wildfly.swarm.examples.jaxrs.shrinkwrap;

    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.jaxrs.JaxRsDeployment;
    
    public class Main {
    
        public static void main(String[] args) throws Exception {
            Container container = new Container();
    
            JaxRsDeployment deployment = new JaxRsDeployment();
            deployment.addResource(MyResource.class);
    
            container.start(deployment);
        }
    }

This method constructs a new default Container, which automatically
initializes all fractions (or subsystems) that are available.

A `JaxRsDeployment` is constructed, and the JAX-RS resource class is
added to it.

The container is then started with the deployment.

By default, if no JAX-RS `Application` is provided a default is added
to the deployment specifying an `@ApplicationPath("/")` to bind the
deployment to the root URL.

## Build

    mvn package

## Run

    java -jar ./target/wildfly-swarm-example-jaxrs-shrinkwrap-1.0.0.Beta1-SNAPSHOT-swarm.jar


## Use

    http://localhost:8080/





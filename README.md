# JAX-RS & Messaging

This examples uses JAX-RS resource implementations and deploys
them through a user-provided `main()` programatically without
construction a `.war` file during the build.

Additionally, it configures a JMS server and sets up some
JMS destinations for use by the JAX-RS resource.

It also deploys an MSC service to consume messages from
the destination.  

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
                    <mainClass>org.wildfly.swarm.examples.messaging.Main</mainClass>
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
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-messaging</artifactId>
        <version>${version.wildfly-swarm}</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-msc</artifactId>
        <version>${version.wildfly-swarm}</version>
        <scope>provided</scope>
    </dependency>

## Project `main()`

This project supplies a `main()` in order to configure the messaging
subsystem and deploy all the pieces of the application.

    package org.wildfly.swarm.examples.messaging;

    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.jaxrs.JaxRsDeployment;
    import org.wildfly.swarm.messaging.MessagingFraction;
    import org.wildfly.swarm.messaging.MessagingServer;
    import org.wildfly.swarm.msc.ServiceDeployment;
    
    public class Main {
    
        public static void main(String[] args) throws Exception {
            Container container = new Container();
    
            container.subsystem(new MessagingFraction()
                            .server(
                                    new MessagingServer()
                                            .enableInVmConnector()
                                            .topic("my-topic")
                                            .queue("my-queue")
                            )
            );
    
            container.start();
    
            JaxRsDeployment appDeployment = new JaxRsDeployment();
            appDeployment.addResource(MyResource.class);
    
            container.deploy(appDeployment);
    
            ServiceDeployment deployment = new ServiceDeployment();
            deployment.addService(new MyService("/jms/topic/my-topic" ) );
    
            container.deploy( deployment );
    
        }
    }

After the container is instantiated, the Messaging fraction is
configured and installed, enabling the in-vm connector and setting
up a topic and a queue.  

The container is started.

A JAX-RS deployment based on a project class is deployed, as is an
instance of an MSC service (which consumes messages from the destination
named in the constructor).
    
## Build

    mvn package

## Run

    java -jar ./target/wildfly-swarm-example-jaxrs-shrinkwrap-1.0.0.Beta1-SNAPSHOT-swarm.jar


## Use

    http://localhost:8080/

On the console the MSC service will print the message it received over JMS

    2015-05-04 14:05:11,457 ERROR [stderr] (Thread-2 (HornetQ-client-global-threads-316630753)) received: Hello!





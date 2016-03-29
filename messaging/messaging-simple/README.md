# JAX-RS & Messaging

This examples uses JAX-RS resource implementations and deploys
them through a user-provided `main()` programatically without
construction a `.war` file during the build.

Additionally, it configures a JMS server and sets up some
JMS destinations for use by the JAX-RS resource.

It also deploys an MSC service to consume messages from
the destination.  

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/SWARM

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
        <mainClass>org.wildfly.swarm.examples.messaging.Main</mainClass>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>package</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

To define the needed parts of WildFly Swarm, some dependencies are added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>jaxrs</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>messaging</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>msc</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

## Project `main()`

This project supplies a `main()` in order to configure the messaging
subsystem and deploy all the pieces of the application.

    package org.wildfly.swarm.examples.messaging;
    
    import org.jboss.shrinkwrap.api.ShrinkWrap;
    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.jaxrs.JAXRSArchive;
    import org.wildfly.swarm.messaging.MessagingFraction;
    import org.wildfly.swarm.msc.ServiceActivatorArchive;
    import org.wildfly.swarm.spi.api.JARArchive;
    
    /**
     * @author Bob McWhirter
     */
    public class Main {
    
        public static void main(String[] args) throws Exception {
            Container container = new Container();
    
            container.fraction(MessagingFraction.createDefaultFraction()
                    .defaultServer((s) -> {
                        s.jmsTopic("my-topic");
                        s.jmsQueue("my-queue");
                    }));
    
            // Start the container
            container.start();

            JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
            appDeployment.addResource(MyResource.class);

            // Deploy your app
            container.deploy(appDeployment);

            JARArchive deployment = ShrinkWrap.create(JARArchive.class);
            deployment.addClass(MyService.class);
            deployment.as(ServiceActivatorArchive.class).addServiceActivator(MyServiceActivator.class);

            // Deploy the services
            container.deploy(deployment);

        }
    }

After the container is instantiated, the Messaging fraction is
configured and installed, enabling the in-vm connector and setting
up a topic and a queue.  

The container is started.

A JAX-RS deployment based on a project class is deployed, as is the
MSC service-activator, which activates a service to consume messages.

You can run it many ways:

* mvn package && java -jar ./target/wildfly-swarm-example-messaging-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.messaging.Main` class

## Use

    http://localhost:8080/

On the console the MSC service will print the message it received over JMS

    2015-05-04 14:05:11,457 INFO [stdout] (Thread-2 (HornetQ-client-global-threads-316630753)) received: Hello!

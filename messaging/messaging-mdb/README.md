# JAX-RS & Messaging MDB

This examples uses JAX-RS resource implementations and deploys
them through a user-provided `main()` programatically without
construction a `.war` file during the build.

Additionally, it configures a JMS server and sets up some
JMS destinations for use by the JAX-RS resource.

It also deploys an MDB to consume messages from
the destination.

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/SWARM

## Project `pomx.xml`

The project is a normal maven project with `jar` packaging, not `war`.

``` xml
<packaging>jar</packaging>
```

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

``` xml
<plugin>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>wildfly-swarm-plugin</artifactId>
  <version>${version.wildfly-swarm}</version>
  <configuration>
    <mainClass>org.wildfly.swarm.examples.messaging.mdb.Main</mainClass>
  </configuration>
  <executions>
    <execution>
      <goals>
        <goal>package</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

To define the needed parts of WildFly Swarm, some dependencies are added

``` xml
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>wildfly-swarm-jaxrs-weld</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>wildfly-swarm-messaging</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>wildfly-swarm-ejb</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
```

## Project `main()`

This project supplies a `main()` in order to configure the messaging
subsystem and deploy all the pieces of the application.

``` java
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.messaging.MessagingFraction;

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

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());

        // Deploy your app
        container.deploy(deployment);
    }
}
```

After the container is instantiated, the Messaging fraction is
configured and installed, enabling the in-vm connector and setting
up a topic and a queue.  

The container is started.

A JAX-RS deployment based on a project class is deployed.

You can run it many ways:

* mvn package && java -jar ./target/wildfly-swarm-example-messaging-mdb-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.messaging.mdb.Main` class

## Use

    http://localhost:8080/

On the console the MDB will print the message it received over JMS

``` sh
2015-10-08 05:05:43,432 ERROR [stderr] (Thread-1 (ActiveMQ-client-global-threads-682848512)) received: Hello!
```
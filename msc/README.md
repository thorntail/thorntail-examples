# JBoss MSC Example

This example takes a simple `.jar` build and deploys JBoss MSC 
(Modular Service Container) service-activators from a user-provided `main()`.

> Please raise any issues found with this example in this repo:
> https://github.com/wildfly-swarm/wildfly-swarm-examples
>
> Issues related to WildFly Swarm core should be raised in the main repo:
> https://github.com/wildfly-swarm/wildfly-swarm/issues

## Project `pom.xml`

This project is a tradition simple `.jar` project, with maven packaging
of `jar`

    <packaging>jar</packaging>

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

    <plugin>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-plugin</artifactId>
      <version>${version.wildfly-swarm}</version>
      <configuration>
        <mainClass>org.wildfly.swarm.examples.msc.Main</mainClass>
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
        <artifactId>wildfly-swarm-msc</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

## Project `main()`

Since this project deploys an MSC `ServiceActivator` which installs
MSC services.

    package org.wildfly.swarm.examples.msc;

    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.msc.ServiceActivatorDeployment;

    public class Main {

        public static void main(String[] args) throws Exception {
            Container container = new Container();
                container.setArgs(args);
                container.start();
    
            ServiceActivatorDeployment deployment = new ServiceActivatorDeployment(container);
    
            deployment.addServiceActivator( MyServiceActivator.class );
            deployment.addClass( MyService.class );
    
            container.deploy( deployment );
        }
    }


This demonstrates starting the container without any deployments,
and then deploying the required classes.  

## Run

* mvn package && java -jar ./target/wildfly-swarm-example-msc-swarm.jar
* mvn wildfly-swarm:run
* From your IDE, run class `org.wildfly.swarm.examples.msc.Main`

## Use

Watch the console for messages on STDERR.

    15:55:08,557 INFO  [org.jboss.msc] (main) JBoss MSC version 1.2.4.Final
    15:55:08,664 INFO  [org.jboss.as] (MSC service thread 1-6) WFLYSRV0049: WildFly Core 1.0.0.CR1 "Kenny" starting
    2015-05-27 15:55:09,030 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Core 1.0.0.CR1 "Kenny" started in 416ms - Started 26 of 31 services (5 services are lazy, passive or on-demand)
    2015-05-27 15:55:09,081 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-5) WFLYSRV0027: Starting deployment of "services.jar" (runtime-name: "services.jar")
    2015-05-27 15:55:09,081 ERROR [stderr] (MSC service thread 1-5) Args available to services: []
    2015-05-27 15:55:09,114 ERROR [stderr] (Thread-25) Howdy #2
    2015-05-27 15:55:09,114 ERROR [stderr] (Thread-23) Hi #1
    2015-05-27 15:55:09,115 ERROR [stderr] (Thread-24) Hi #2
    2015-05-27 15:55:09,115 ERROR [stderr] (Thread-26) Howdy #1
    2015-05-27 15:55:09,116 INFO  [org.jboss.as.server] (main) WFLYSRV0010: Deployed "services.jar" (runtime-name : "services.jar")
    2015-05-27 15:55:10,119 ERROR [stderr] (Thread-25) Howdy #2
    2015-05-27 15:55:10,119 ERROR [stderr] (Thread-24) Hi #2
    2015-05-27 15:55:10,119 ERROR [stderr] (Thread-26) Howdy #1
    2015-05-27 15:55:10,120 ERROR [stderr] (Thread-23) Hi #1
    2015-05-27 15:55:11,124 ERROR [stderr] (Thread-24) Hi #2
    2015-05-27 15:55:11,124 ERROR [stderr] (Thread-26) Howdy #1
    2015-05-27 15:55:11,124 ERROR [stderr] (Thread-23) Hi #1
    2015-05-27 15:55:11,125 ERROR [stderr] (Thread-25) Howdy #2
    2015-05-27 15:55:12,128 ERROR [stderr] (Thread-23) Hi #1
    2015-05-27 15:55:12,128 ERROR [stderr] (Thread-26) Howdy #1
    2015-05-27 15:55:12,128 ERROR [stderr] (Thread-25) Howdy #2
    2015-05-27 15:55:12,129 ERROR [stderr] (Thread-24) Hi #2
    2015-05-27 15:55:13,129 ERROR [stderr] (Thread-25) Howdy #2
    2015-05-27 15:55:13,130 ERROR [stderr] (Thread-26) Howdy #1
    2015-05-27 15:55:13,133 ERROR [stderr] (Thread-24) Hi #2
    2015-05-27 15:55:13,133 ERROR [stderr] (Thread-23) Hi #1

# JBoss MSC Example

This example takes a simple `.jar` build and deploys
JBoss MSC (Modular Service Container) services by 
instances from a user-provided `main()`.

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
                    <mainClass>org.wildfly.swarm.examples.msc.Main</mainClass>
                </manifest>
            </archive>
        </configuration>
    </plugin>

To define the needed parts of WildFly Swarm, a dependency is added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-msc</artifactId>
        <version>${version.wildfly-swarm}</version>
        <scope>provided</scope>
    </dependency>

## Project `main()`

Since this project deploys instances of MSC services, a user-provided
`main()` is used to instantiate the container the the services.

The service instances's own lifecycle (`start()` and `stop()`) are called
once they are deployed into the container.

    package org.wildfly.swarm.examples.msc;

    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.msc.ServiceDeployment;
    
    public class Main {
    
        public static void main(String[] args) throws Exception {
            Container container = new Container();
    
            container.start();
    
            ServiceDeployment deployment = new ServiceDeployment();
            deployment.addService( new MyService("hi!" ) );
            deployment.addService( new MyService("howdy!" ) );
            container.deploy( deployment );
    
            deployment = new ServiceDeployment();
            deployment.addService( new MyService("hi #2!" ) );
            deployment.addService( new MyService("howdy #2!" ) );
            container.deploy( deployment );
        }
    }

This demonstrates starting the container without any deployments,
and then deploying multiple deployments into it.  In this case,
each deployment is two distinct instances of a single MSC service
class, parameterized though its constructor.


## Build

    mvn package

## Run

    java -jar ./target/wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-swarm.jar


## Use

Watch the console for messages on STDERR.

    13:20:39,421 INFO  [org.jboss.msc] (main) JBoss MSC version 1.2.4.Final
    13:20:39,522 INFO  [org.jboss.as] (MSC service thread 1-6) WFLYSRV0049: WildFly Core 1.0.0.CR1 "Kenny" starting
    13:20:39,825 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Core 1.0.0.CR1 "Kenny" started in 356ms - Started 26 of 31 services (5 services are lazy, passive or on-demand)
    13:20:39,878 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) WFLYSRV0027: Starting deployment of "wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-service-1.jar" (runtime-name: "wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-service-1.jar")
    13:20:39,923 ERROR [stderr] (Thread-21) howdy!
    13:20:39,923 ERROR [stderr] (Thread-22) hi!
    13:20:39,928 INFO  [org.jboss.as.server] (main) WFLYSRV0010: Deployed "wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-service-1.jar" (runtime-name : "wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-service-1.jar")
    13:20:39,937 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-6) WFLYSRV0027: Starting deployment of "wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-service-2.jar" (runtime-name: "wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-service-2.jar")
    13:20:39,945 ERROR [stderr] (Thread-23) hi #2!
    13:20:39,945 ERROR [stderr] (Thread-24) howdy #2!
    13:20:39,951 INFO  [org.jboss.as.server] (main) WFLYSRV0010: Deployed "wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-service-2.jar" (runtime-name : "wildfly-swarm-example-msc-1.0.0.Beta1-SNAPSHOT-service-2.jar")
    13:20:40,926 ERROR [stderr] (Thread-21) howdy!
    13:20:40,927 ERROR [stderr] (Thread-22) hi!
    13:20:40,950 ERROR [stderr] (Thread-23) hi #2!
    13:20:40,950 ERROR [stderr] (Thread-24) howdy #2!
    13:20:41,928 ERROR [stderr] (Thread-21) howdy!
    13:20:41,929 ERROR [stderr] (Thread-22) hi!
    13:20:41,952 ERROR [stderr] (Thread-23) hi #2!
    13:20:41,952 ERROR [stderr] (Thread-24) howdy #2!







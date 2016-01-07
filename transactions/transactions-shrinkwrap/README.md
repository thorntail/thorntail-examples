# JAX-RS & Transactions

This example uses a JAX-RS resource and UserTransaction to start
and end transactions.

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
        <mainClass>org.wildfly.swarm.examples.transactions.Main</mainClass>
      </configuration>
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
        <artifactId>wildfly-swarm-jaxrs</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-transactions</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

## Project `main()`

Since this project deploys JAX-RS resources without a `.war` being constructed,
it provides its own `main()` method (specified above via the `wildfly-swarm-plugin`) to
configure the container and deploy the resources programmatically.

    package org.wildfly.swarm.examples.transactions;
    
    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.jaxrs.JAXRSDeployment;
    import org.wildfly.swarm.transactions.TransactionsFraction;
    
    public class Main
    {
        public static void main (String[] args) throws Exception 
        {
            Container container = new Container();
    
            container.subsystem(new TransactionsFraction(4712, 4713));
    
            // Start the container
            container.start();
    
            JAXRSDeployment appDeployment = new JAXRSDeployment(container);
            appDeployment.addResource(MyResource.class);
    
            container.deploy(appDeployment);
        }
    }


This demonstrates starting the container without any deployments,
and then deploying the required classes.  

## Run

* mvn package && java -jar ./target/wildfly-swarm-example-transactions-swarm.jar
* mvn wildfly-swarm:run
* From your IDE, run class `org.wildfly.swarm.examples.transactions.Main`

## Use

    http://localhost:8080/

The browser will print the message ...

    Active

Then try

    http://localhost:8080/begincommit

The browser output should be ...

    Transaction begun ok and committed ok

Next

    http://localhost:8080/beginrollback

And we'll see ...

    Transaction begun ok and rolled back ok

Finally try

    http://localhost:8080/nested

And you'll see ...

    Nested transaction support is not enabled!

Of course if you've enabled nested transactions in JTA on WildFly then you'll see something different!
# JAX-RS & Arjuna Transactions

This example uses a JAX-RS resource and AtomicAction & friends.

Note, these are not Java EE compliant APIs and classes we're going to use. But that's the point!

## Project `pom.xml`

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

To define the needed parts of WildFly Swarm, a dependency is added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>jaxrs</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>transactions</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

## Build

    mvn package

## Run

    java -jar ./target/example-arjuna-swarm.jar

## Use

    First try ...

    http://localhost:8080/atomicaction

    In the browser you should see something like ...

    Begin BasicAction: 0:ffffac1c8001:2fb05ec4:567ed3a5:b status: ActionStatus.RUNNING
    Committed BasicAction: 0:ffffac1c8001:2fb05ec4:567ed3a5:b status: ActionStatus.COMMITTED

    Then try ...

    http://localhost:8080/begincommit

    Should result in ...

    Transaction begun ok and committed ok

    Now try ...

    http://localhost:8080/beginrollback

    You should get something like the following in your browser ...

    Transaction begun ok and rolled back ok

    Now let's try something which isn't typically supported in Java EE applications ... Nested Transactions! Yes, you can have an arbitrarily nested transaction hierarchy and even if child transactions commit, their work will still be undone (aborted) if an enclosing parent transaction later rolls back. Likewise, the impact of aborting a child transaction does not have to impact the outcome of a parent transaction.

    So go to your browser and try ...

    http://localhost:8080/nestedChildCommit

    You should see something like ...

    Nested transaction  BasicAction: 0:ffffc0a80102:-71b7e35b:5a3e32e2:e status: ActionStatus.RUNNING started!
    Child and parent committed ok!

    Let's try to abort the child transaction whilst still committing the parent. So go to ...

    http://localhost:8080/nestedChildAbort

    And you should see ...

    Nested transaction  BasicAction: 0:ffffc0a80102:-71b7e35b:5a3e32e2:12 status: ActionStatus.RUNNING started!
    Child aborted and parent still committed ok!
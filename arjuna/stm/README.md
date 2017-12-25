# JAX-RS & Arjuna Software Transactional Memory

This example uses a JAX-RS resource and AtomicAction & friends.

Note, these are not Java EE compliant APIs and classes we're going to use. But that's the point!

## Project `pom.xml`

The project is a normal maven project with `war` packaging, not `jar`.

    <packaging>war</packaging>

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the `.war`.

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

Additionally, the usual `maven-war-plugin` is provided.

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <configuration>
          <failOnMissingWebXml>false</failOnMissingWebXml>
        </configuration>
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

   mvn wildfly-swarm:run

## Use

    First try ...

    http://localhost:8080/stm

    In the browser you should see something like ...

Object name: 0:ffffc0a80102:-48ffece4:5a3fb241:d
Transactional object value initially 10
Transactional object value incremented to 11
Transactional object value incremented to 12
Transactional object value incremented to 13
Transactional object value incremented to 14
Transactional object value incremented to 15
Transactional object value incremented to 16
Transactional object value incremented to 17
Transactional object value incremented to 18
Transactional object value incremented to 19
Transactional object value incremented to 20

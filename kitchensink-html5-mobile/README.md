# WildFly kitchensink-html5-mobile Example

This example takes the [kitchensick-html5-mobile](https://github.com/wildfly/quickstart/tree/master/kitchensink-html5-mobile)
quickstart from WildFly, and wraps it into a `-swarm` runnable jar.

> Please raise any issues found with this example in this repo:
> https://github.com/wildfly-swarm/wildfly-swarm-examples
>
> Issues related to WildFly Swarm core should be raised in the main repo:
> https://github.com/wildfly-swarm/wildfly-swarm/issues

## Project `pom.xml`

This project is a traditional CDI, JPA, JAX-RS, EJB project, with maven packaging
of `war` in the `pom.xml`

    <packaging>war</packaging>

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

    <plugin>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-plugin</artifactId>
      <version>${version.wildfly-swarm}</version>
      <executions>
        <execution>
          <goals>
            <goal>package</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

To define the needed parts of WildFly Swarm, the following dependencies are added

    <dependency>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-weld-jaxrs</artifactId>
      <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-jpa</artifactId>
      <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-ejb</artifactId>
      <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-hibernate-validator</artifactId>
      <version>${version.wildfly-swarm}</version>
    </dependency>

This dependency provides the EJB, JPA, JAX-RS, and CDI APIs to your application, so the
project does *not* need to specify those.

Additional application dependencies can be
specified and will be included in the normal `.war` construction and available
within the WildFly Swarm application `.jar`.

## Run

You can run it many ways:

* mvn package && java -jar ./target/wildfly-swarm-example-kitchensink-html5-mobile-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.Swarm` class

## Use

Since WildFly Swarm apps tend to support one deployment per executable, it
automatically adds a `jboss-web.xml` to the deployment if it doesn't already
exist.  This is used to bind the deployment to the root of the web-server,
instead of using the `.war`'s own name as the application context.

    http://localhost:8080/

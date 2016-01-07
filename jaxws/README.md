# JAX-WS .war Example

This example takes a normal JAX-WS build, and wraps it into
a `-swarm` runnable jar.

It is originally from [wildfly-quickstart helloworld-ws](https://github.com/wildfly/quickstart/tree/10.x/helloworld-ws).

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/SWARM

## Project `pom.xml`

This project is a traditional JAX-RS project, with maven packaging
of `war` in the `pom.xml`

``` xml
<packaging>war</packaging>
```

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

``` xml
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
```

To define the needed parts of WildFly Swarm, a dependency is added

``` xml
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>webservices</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
```

This dependency provides the JAX-WS APIs to your application, so the
project does *not* need to specify those.

## Run

* mvn package && java -jar ./target/example-jaxws-swarm.jar
* mvn wildfly-swarm:run
* From your IDE, run class `org.wildfly.swarm.Swarm`

## Use

Since WildFly Swarm apps tend to support one deployment per executable, it
automatically adds a `jboss-web.xml` to the deployment if it doesn't already
exist.  This is used to bind the deployment to the root of the web-server,
instead of using the `.war`'s own name as the application context.

```
http://localhost:8080/
```

The above resource displays the `wsdl` link.
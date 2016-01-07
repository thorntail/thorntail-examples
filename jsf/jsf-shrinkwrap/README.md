# JSF & ShrinkWrap Example

This examples uses JSF resource implementations and deploys
them through a user-provided `main()` programatically without
construction a `.war` file during the build.

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/SWARM

## Project `pom.xml`

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
    <mainClass>org.wildfly.swarm.examples.jsf.shrinkwrap.Main</mainClass>
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

To define the needed parts of WildFly Swarm, a dependency is added

``` xml
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>jsf</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>weld</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
```

These dependencies allow usage of ShrinkWrap APIs within the `main()` in addition
to providing the JSF and CDI APIs.

## Project `main()`

Since this project deploys JSF resources without a `.war` being construction, it
provides its own `main()` method (specified above via the `wildfly-swarm-plugin`) to
configure the container and deploy the resources programatically.

``` java
package org.wildfly.swarm.examples.jsf.shrinkwrap;

import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.undertow.DefaultWarDeployment;
import org.wildfly.swarm.undertow.WarDeployment;

public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        WarDeployment deployment = new DefaultWarDeployment(container);

        deployment.getArchive().addClass(Message.class);

        deployment.getArchive().addAsWebResource(
                new ClassLoaderAsset("index.html", Main.class.getClassLoader()), "index.html");
        deployment.getArchive().addAsWebResource(
                new ClassLoaderAsset("index.xhtml", Main.class.getClassLoader()), "index.xhtml");

        deployment.getArchive().addAsWebInfResource(
                new ClassLoaderAsset("WEB-INF/web.xml", Main.class.getClassLoader()), "web.xml");
        deployment.getArchive().addAsWebInfResource(
                new ClassLoaderAsset("WEB-INF/template.xhtml", Main.class.getClassLoader()), "template.xhtml");

        container.start().deploy(deployment);

    }
}
```

This method constructs a new default Container, which automatically
initializes all fractions (or subsystems) that are available.

You will need to add the xhtml files to Shrinkwrap in a manner such as `deployment.addAsWebResource()` since JSF is non static.

The container is then started with the deployment.

## Run

You can run it many ways:

* mvn package && java -jar ./target/example-jsf-shrinkwrap-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.jsf.shrinkwrap.Main` class

## Use

http://localhost:8080/

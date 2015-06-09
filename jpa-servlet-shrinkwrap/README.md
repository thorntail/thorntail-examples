# JPA and Servlet with Shrinkwrap Example

This example uses JPA and Servlet classes deployed through a user-provided
`main()` programmatically without constructing a `.war` file during the build.

> Please raise any issues found with this example on the main project:
> https://github.com/wildfly-swarm/wildfly-swarm/issues

## Project `pom.xml`

This project is a normal maven project with `jar` packaging, not `war`.

    <packaging>jar</packaging>

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

    <plugin>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>wildfly-swarm-plugin</artifactId>
      <version>${version.wildfly-swarm}</version>
      <configuration>
        <mainClass>org.wildfly.swarm.examples.jpa.Main</mainClass>
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
        <artifactId>wildfly-swarm-jpa</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>wildfly-swarm-undertow</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

The `wildfly-swarm-jpa` dependency allows usage of ShrinkWrap APIs within the `main()`,
provides configuration classes for adding the JDBC driver and datasources to the container,
in addition to providing the JPA APIs.  The `wildfly-swarm-undertow` provides the Servlet
APIs.

Additionally, the JDBC driver jar you wish to deploy is specified as a dependency
within your `pom.xml`

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>1.4.187</version>
    </dependency>

## Project `main()`

Since this project deploys Servlet resources without a `.war` being constructed, it
provides its own `main()` method (specified above via the `wildfly-swarm-plugin`) to
configure the container and deploy the resources programmatically. Additionally,
it deploys the JDBC driver jar using a simplified Maven GAV (no version is required)
and deploys a datasource.

    package org.wildfly.swarm.examples.jpa;
    
    import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.container.DefaultWarDeployment;
    import org.wildfly.swarm.container.WarDeployment;
    import org.wildfly.swarm.datasources.Datasource;
    import org.wildfly.swarm.datasources.DatasourceDeployment;
    import org.wildfly.swarm.datasources.DriverDeployment;
    
    public class Main {
        public static void main(String[] args) throws Exception {
            Container container = new Container();
            container.start();
    
            DriverDeployment driverDeployment = new DriverDeployment(container, "com.h2database:h2", "h2");
            container.deploy(driverDeployment);
    
            DatasourceDeployment dsDeployment = new DatasourceDeployment(container, new Datasource("ExampleDS")
                    .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                    .driver("h2")
                    .authentication("sa", "sa")
            );
            container.deploy(dsDeployment);
    
            WarDeployment deployment = new DefaultWarDeployment(container);
            deployment.getArchive().addClasses(Employee.class);
            deployment.getArchive().addClass(EmployeeServlet.class);
            deployment.getArchive().addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
            deployment.getArchive().addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");
    
            container.deploy(deployment);
        }
    }

This method constructs a new default Container, which automatically
initializes all fractions (or subsystems) that are available.

A `DriverDeployment` is created, specifying the `groupId` and `artifactId` portions
of the maven dependency (the version is inferred from your `pom.xml`), along with
a name for referencing the driver.

A datasource is then deployed using the previously-deployed driver.

JNDI names are bound automatically.

A `DefaultWarDeployment` is constructed, and the JPA Entity class, Servlet class,
`persistence.xml` and `load.sql` files are added to it.

## Run

You can run it many ways:

* mvn package && java -jar ./target/wildfly-swarm-example-jaxrs-servlet-1.0.0.Beta1-SNAPSHOT-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.jpa.Main` class

## Use

    http://localhost:8080/

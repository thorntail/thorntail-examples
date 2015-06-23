# JPA, JAX-RS and CDI with Shrinkwrap Example

This example uses JPA, CDI and JAX-RS resources deployed through a user-provided
`main()` programmatically without constructing a `.war` file during the build.

> Please raise any issues found with this example in this repo:
> https://github.com/wildfly-swarm/wildfly-swarm-examples
>
> Issues related to WildFly Swarm core should be raised in the main repo:
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
        <artifactId>wildfly-swarm-weld-jaxrs</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

The `wildfly-swarm-jpa` dependency allows usage of ShrinkWrap APIs within the `main()`,
provides configuration classes for adding the JDBC driver and datasources to the container,
in addition to providing the JPA APIs.  The `wildfly-swarm-weld-jaxrs` provides the CDI and JAX-RS
APIs.

Additionally, the JDBC driver jar you wish to deploy is specified as a dependency
within your `pom.xml`

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>1.4.187</version>
    </dependency>

## Project `main()`

Since this project deploys CDI and JAX-RS resources without a `.war` being constructed, it
provides its own `main()` method (specified above via the `wildfly-swarm-plugin`) to
configure the container and deploy the resources programmatically. Additionally,
it deploys the JDBC driver jar using a simplified Maven GAV (no version is required)
and deploys a datasource.

    package org.wildfly.swarm.examples.jpa;
    
    import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.datasources.Datasource;
    import org.wildfly.swarm.datasources.DatasourcesFraction;
    import org.wildfly.swarm.datasources.Driver;
    import org.wildfly.swarm.jaxrs.JAXRSDeployment;
    import org.wildfly.swarm.jpa.JPAFraction;
    
    public class Main {
        public static void main(String[] args) throws Exception {
            Container container = new Container();
    
            container.subsystem(new DatasourcesFraction()
                            .driver(new Driver("h2")
                                    .datasourceClassName("org.h2.Driver")
                                    .xaDatasourceClassName("org.h2.jdbcx.JdbcDataSource")
                                    .module("com.h2database.h2"))
                            .datasource(new Datasource("MyDS")
                                    .driver("h2")
                                    .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                                    .authentication("sa", "sa"))
            );
    
            // Prevent JPA Fraction from installing it's default datasource fraction
            container.fraction(new JPAFraction()
                            .inhibitDefaultDatasource()
                            .defaultDatasourceName("MyDS")
            );
    
            container.start();
    
            JAXRSDeployment deployment = new JAXRSDeployment(container);
            deployment.getArchive().addClasses(Employee.class);
            deployment.getArchive().addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
            deployment.getArchive().addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");
            deployment.addResource(EmployeeResource.class);
    
            container.deploy(deployment);
        }
    }

This method constructs a new default Container, which automatically
initializes all fractions (or subsystems) that are available.

The datasources fraction has no particular default configuration, so by providing a
specific configuration we enable a driver and a datasource.

JNDI names are bound automatically.

We prevent the JPA fraction from automatically configuring a default driver and datasource
as we want to define that ourselves.

The empty container is started.

A `JAXRSDeployment` is constructed, and the JAX-RS resource class is
added to it, along with the JPA Entity class, persistence.xml and load.sql.

By default, if no JAX-RS `Application` is provided a default is added
to the deployment specifying an `@ApplicationPath("/")` to bind the
deployment to the root URL.

## Run

You can run it many ways:

* mvn package && java -jar ./target/wildfly-swarm-example-jpa-jaxrs-cdi-shrinkwrap-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.jpa.Main` class

## Use

    http://localhost:8080/
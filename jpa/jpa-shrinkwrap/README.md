# JPA and Servlet with Shrinkwrap Example

This example uses JPA and Servlet classes deployed through a user-provided
`main()` programmatically without constructing a `.war` file during the build.

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/SWARM

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
        <mainClass>org.wildfly.swarm.examples.jpa.shrinkwrap.Main</mainClass>
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
        <artifactId>jpa</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>
    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>undertow</artifactId>
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
    import org.wildfly.swarm.datasources.Datasource;
    import org.wildfly.swarm.datasources.DatasourcesFraction;
    import org.wildfly.swarm.datasources.Driver;
    import org.wildfly.swarm.jpa.JPAFraction;
    import org.wildfly.swarm.undertow.WARArchive;
    
    public class Main {
        public static void main(String[] args) throws Exception {
            Container container = new Container();
    
            container.subsystem(new DatasourcesFraction()
                            .driver(new Driver("h2")
                                    .driverClassName("org.h2.Driver")
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
    
            WARArchive deployment = ShrinkWrap.create(WARArchive.class);
            deployment.addClasses(Employee.class);
            deployment.addClass(EmployeeServlet.class);
            deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
            deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");
    
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

A `DefaultWarDeployment` is constructed, and the JPA Entity class, Servlet class,
`persistence.xml` and `load.sql` files are added to it.

## Run

You can run it many ways:

* mvn package && java -jar ./target/jpa-shrinkwrap-swarm.jar
* mvn wildfly-swarm:run
* From your IDE, run class `org.wildfly.swarm.examples.jpa.shrinkwrap.Main`

## Use

    http://localhost:8080/

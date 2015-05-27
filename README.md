# Datasource via Deployment.

This example builds upon the JAX-RS/ShrinkWrap example to also
deploy a JDBC datasource as an additional deployment.

> Please raise any issues found with this example on the main project:
> https://github.com/wildfly-swarm/wildfly-swarm/issues

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
        <mainClass>org.wildfly.swarm.examples.ds.deployment.Main</mainClass>
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
        <artifactId>wildfly-swarm-datasources</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

The `wildfly-swarm-jaxrs` dependency allows usage of ShrinkWrap APIs within the `main()` in addition
to providing the JAX-RS APIs.  The `wildfly-swarm-datasources` dependency provides configuration
classes for adding the JDBC driver and datasources to the container.

Additionally, the JDBC driver jar you wish to deploy is specified as a dependency
within your `pom.xml`

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>1.4.187</version>
    </dependency>

## Project `main()`

Since this project deploys JAX-RS resources without a `.war` being construction, it
provides its own `main()` method (specified above via the `maven-jar-plugin`) to
configure the container and deploy the resources programatically. Additionally,
it deploys the JDBC driver jar using simplified Maven GAV (no version is required)
and deploys a datasource.

    package org.wildfly.swarm.examples.ds.deployment;
    
    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.datasources.Datasource;
    import org.wildfly.swarm.datasources.DatasourceDeployment;
    import org.wildfly.swarm.datasources.DriverDeployment;
    import org.wildfly.swarm.jaxrs.JAXRSDeployment;
    
    /**
     * @author Bob McWhirter
     */
    public class Main {
    
        public static void main(String[] args) throws Exception {
    
            Container container = new Container();
    
            container.start();
    
            DriverDeployment driverDeployment = new DriverDeployment( container, "com.h2database:h2", "h2" );
    
            container.deploy(driverDeployment);
    
            DatasourceDeployment dsDeployment = new DatasourceDeployment(container, new Datasource("ExampleDS")
                    .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                    .driver("h2" )
                    .authentication("sa", "sa")
            );
    
            container.deploy( dsDeployment );
    
            JAXRSDeployment appDeployment = new JAXRSDeployment(container);
            appDeployment.addResource(MyResource.class);
    
            container.deploy(appDeployment);
    
        }
    }

This method constructs a new default Container, which automatically
initializes all fractions (or subsystems) that are available.  

A `DriverDeployment` is created, specifying the `groupId` and `artifactId` portions
of the maven dependency (the version is inferred from your `pom.xml`), along with
a name for referencing the driver.

A datasource is then deployed using the previously-deployed driver.

JNDI names are bound automatically.

A `JAXRSDeployment` is constructed, and the JAX-RS resource class is
added to it.

The resource looks up the Datasource through JNDI at run-time:

    @Path( "/" )
    public class MyResource {

        @GET
        @Produces( "text/plain" )
        public String get() throws NamingException, SQLException {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup( "jboss/datasources/ExampleDS" );
            Connection conn = ds.getConnection();
            try {
                return "Howdy using connection: " + conn;
            } finally {
                conn.close();
            }
        }
    }


## Run

You can run it many ways:

* mvn package && java -jar ./target/wildfly-swarm-example-datasource-deployment-1.0.0.Beta1-SNAPSHOT-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.ds.deployment.Main` class

## Use

    http://localhost:8080/

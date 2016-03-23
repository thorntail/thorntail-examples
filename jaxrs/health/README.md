# JAX-RS Health Endpoints

See also the userguide section for monitoring and security realms:
https://wildfly-swarm.gitbooks.io/wildfly-swarm-users-guide/content/advanced/monitoring.html

This example uses the `monitor` and `management` fractions
so that the JAX-RS endpoints can securely expose health checks to service registries.

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
        <mainClass>org.wildfly.swarm.examples.jaxrs.health.Main</mainClass>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>package</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

To define the needed parts of WildFly Swarm, dependencies are added

    <dependency>
        <groupId>org.wildfly.swarm</groupId>
        <artifactId>jaxrs</artifactId>
        <version>${version.wildfly-swarm}</version>
    </dependency>

    <dependency>
           <groupId>org.wildfly.swarm</groupId>
           <artifactId>monitor</artifactId>
           <version>${version.wildfly-swarm}</version>
    </dependency>


## Project `main()`

Since this project deploys JAX-RS resources without a `.war` being constructed, it
provides its own `main()` method (specified above via the `wildfly-swarm-plugin`) to
configure the container and deploy the resources programatically.

    package org.wildfly.swarm.examples.jaxrs.health;

    [...]

    public class Main {

        public static void main(String[] args) throws Exception {

            Container container = new Container();

                    JAXRSArchive archive = ShrinkWrap.create(JAXRSArchive .class, "healthcheck-app.war");
                    JAXRSArchive deployment = archive.as(JAXRSArchive.class).addPackage(Main.class.getPackage());
                    deployment.addResource(HealthCheckResource.class);
                    deployment.addResource(RegularResource.class);

                    deployment.addAllDependencies();
                    container
                            .fraction(LoggingFraction.createDefaultLoggingFraction())
                            .fraction(new MonitorFraction().securityRealm("ManagementRealm"))
                            .fraction(new ManagementFraction()
                                              .securityRealm("ManagementRealm", (realm) -> {
                                                  realm.inMemoryAuthentication((authn) -> {
                                                      authn.add(new Properties() {{
                                                          put("admin", "password");
                                                      }}, true);
                                                  });
                                                  realm.inMemoryAuthorization();
                                              }))
                            .start()
                            .deploy(deployment);
        }
    }

This method constructs a new default Container, and
adds the monitoring and management fractions that provide the /health endpoints and security to them.

## @Health Annotations

In our example, we annotate the JAX-RS resource to be exposed as a health endpoint:

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    @Health
    public String firstHealthCheckMethod() {
      return "Healthy";
    }

By pointing your browser at http://localhost:8080/health you should be prompted for credentials (see security domain setup) and
be able to retrieve the health response.

## Run

You can run it many ways:

* mvn package && java -jar ./target/example-jaxrs-health-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.jaxrs.health.Main` class

## Use

To `GET` the health response

    http://localhost:8080/health


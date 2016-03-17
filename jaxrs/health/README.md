# JAX-RS & Swagger Example

This example builds on the JAX-RS and ShrinkWrap example.
They both have JAX-RS resource implementations and deploy
them through a user-provided `main()` programatically without
construction a `.war` file during the build.

This example builds on that by adding the `swagger` fraction
so that the JAX-RS API is exposed through the /swagger.json
URL.

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
        <mainClass>org.wildfly.swarm.examples.jaxrs.shrinkwrap.Main</mainClass>
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

This dependency allows usage of ShrinkWrap APIs within the `main()` in addition
to providing the JAX-RS APIs.

For the swagger dependency, the `pom.xml` file has

    <dependency>
      <groupId>org.wildfly.swarm</groupId>
      <artifactId>swagger</artifactId>
      <version>${project.version}</version>
    </dependency>


## Project `main()`

Since this project deploys JAX-RS resources without a `.war` being constructed, it
provides its own `main()` method (specified above via the `wildfly-swarm-plugin`) to
configure the container and deploy the resources programatically.

    package org.wildfly.swarm.examples.jaxrs.swagger;

    import org.jboss.shrinkwrap.api.ShrinkWrap;
    import org.jboss.shrinkwrap.api.exporter.ZipExporter;
    import org.wildfly.swarm.container.Container;
    import org.wildfly.swarm.jaxrs.JAXRSArchive;
    import org.wildfly.swarm.logging.LoggingFraction;
    import org.wildfly.swarm.swagger.SwaggerArchive;
    import org.wildfly.swarm.swagger.SwaggerFraction;

    import java.io.File;

    public class Main {

        public static void main(String[] args) throws Exception {

            Container container = new Container();

            JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "swagger-app.war");
            deployment.addClass(TimeResource.class);

            // Enable the swagger bits
            SwaggerArchive archive = deployment.as(SwaggerArchive.class);
            // Tell swagger where our resources are
            archive.register("org.wildfly.swarm.examples.jaxrs.swagger");

            deployment.addAllDependencies();
            container
                .fraction(LoggingFraction.createDefaultLoggingFraction())
                .start()
                .deploy(deployment);
        }
    }

This method constructs a new default Container, which automatically
initializes all fractions (or subsystems) that are available.

A `JAXRSArchive` is constructed, and the JAX-RS resource class is
added to it.

Swagger is enabled with

    SwaggerArchive archive = deployment.as(SwaggerArchive.class);

The resources to be exposed by `swagger.json` are specified using
a comma-separated list of package names. In this case, we only have
a single package containing JAX-RS resources that we are interested
in exposing, which is specified like so:

    archive.register("org.wildfly.swarm.examples.jaxrs.swagger");

The container is then started with the deployment.

By default, if no JAX-RS `Application` is provided a default is added
to the deployment specifying an `@ApplicationPath("/")` to bind the
deployment to the root URL.

We could modify the above `main()` method with:

    deployment.addClass(MyApp.class);

to provide our own JAX-RS `Application`, which would modify the path to be
`http://localhost:8080/taco`.

## Swagger Annotations

The JAX-RS API in this example is exposed in the `swagger.json` format
by using the annotations provided by swagger itself. See the swagger wiki
for complete documentation https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X.

In our example, we annotate the JAX-RS resource as so:

    @Path("/time")
    @Api(value = "/time", description = "Get the time", tags = "time")
    @Produces(MediaType.APPLICATION_JSON)
    public class TimeResource {

        @GET
        @Path("/now")
        @ApiOperation(value = "Get the current time",
                notes = "Returns the time as a string",
                response = String.class
        )

        @Produces(MediaType.APPLICATION_JSON)
        public String get() {
            return "The time isf " + new DateTime();
        }
    }

By pointing your browser at http://localhost:8080/swagger.json you should see the
API documented similar to this.

    {
        "swagger":"2.0",
        "info":{"version":"1.0.0"},
        "host":"localhost:8080",
        "basePath":"/swagger",
        "tags":[{"name":"time"}],
        "schemes":["http"],
        "paths": {
            "/time/now": {
                "get": {
                    "tags":["time"],
                    "summary":"Get the current time",
                    "description":"Returns the time as a string",
                    "operationId":"get",
                    "produces":["application/json"],
                    "parameters":[],
                    "responses": {
                        "200": {
                            "description":"successful operation",
                            "schema":{"type":"string"}
                        }
                    }
                }
            }
        }
    }

## Run

You can run it many ways:

* mvn package && java -jar ./target/example-jaxrs-swagger-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.jaxrs.swagger..Main` class

## Use

To `GET` the JAX-RS resouce

    http://localhost:8080/time/now

To `GET` the API specification as a `swagger.json` document

    http://localhost:8080/swagger.json

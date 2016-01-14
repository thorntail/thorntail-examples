package org.wildfly.swarm.examples.jaxrs.swagger;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.swagger.SwaggerArchive;

/**
 * @author Lance Ball
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "swagger-app.war");
        deployment.addPackage(Main.class.getPackage());

        // Enable the swagger bits
        SwaggerArchive archive = deployment.as(SwaggerArchive.class);
        // Tell swagger where our resources are
        archive.setResourcePackages("org.wildfly.swarm.examples.jaxrs.swagger");

        deployment.addAllDependencies();
        container
                .fraction(LoggingFraction.createDefaultLoggingFraction())
                .start()
                .deploy(deployment);
    }
}

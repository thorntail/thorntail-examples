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

        SwaggerArchive archive = ShrinkWrap.create(SwaggerArchive.class, "swagger-app.war");
        JAXRSArchive deployment = archive.as(JAXRSArchive.class).addPackage(Main.class.getPackage());


        // Tell swagger where our resources are
        archive.setResourcePackages("org.wildfly.swarm.examples.jaxrs.swagger");

        deployment.addAllDependencies();
        container
                .fraction(LoggingFraction.createDefaultLoggingFraction())
                .start()
                .deploy(deployment);
    }
}

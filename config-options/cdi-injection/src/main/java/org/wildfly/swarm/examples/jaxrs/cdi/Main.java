package org.wildfly.swarm.examples.jaxrs.cdi;

import java.net.URL;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.cdi.CDIFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jaxrs.JAXRSFraction;
import org.wildfly.swarm.logging.LoggingFraction;

/**
 * @author Heiko Braun
 * @since 21/04/16
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ClassLoader cl = Main.class.getClassLoader();
        URL stageConfig = cl.getResource("project-stages.yml");

        assert stageConfig != null : "Failed to load stage configuration";

        Swarm swarm = new Swarm(false)
                .withStageConfig(stageConfig);

        swarm
                .fraction(new JAXRSFraction())
                .fraction(new CDIFraction())
                .fraction(new LoggingFraction());

        // Start the container
        swarm.start();

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(Controller.class);

        // Deploy your app
        swarm.deploy(appDeployment);
    }
}

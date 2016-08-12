package org.wildfly.swarm.examples.messaging.mdb;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.messaging.MessagingFraction;

/**
 * @author Yoshimasa Tanabe
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Swarm swarm = new Swarm();

        swarm.fraction(MessagingFraction.createDefaultFraction()
                        .defaultServer((s) -> {
                            s.jmsQueue("my-queue");
                            s.jmsTopic("my-topic");
                        })
        );

        // Start the container
        swarm.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());

        // Deploy your app
        swarm.deploy(deployment);
    }
}

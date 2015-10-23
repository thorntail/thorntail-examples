package org.wildfly.swarm.examples.messaging.mdb;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.messaging.MessagingFraction;

/**
 * @author Yoshimasa Tanabe
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.fraction(MessagingFraction.createDefaultFraction()
                .defaultServer((s) -> {
                    s.jmsTopic("my-topic");
                    s.jmsQueue("my-queue");
                }));

        // Start the container
        container.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());

        // Deploy your app
        container.deploy(deployment);
    }
}

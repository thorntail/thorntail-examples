package org.wildfly.swarm.examples.messaging;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JaxRsDeployment;
import org.wildfly.swarm.messaging.MessagingFraction;
import org.wildfly.swarm.messaging.MessagingServer;
import org.wildfly.swarm.msc.ServiceDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.subsystem(new MessagingFraction()
                        .server(
                                new MessagingServer()
                                        .enableInVmConnector()
                                        .topic("my-topic")
                                        .queue("my-queue")
                        )
        );

        // Start the container
        container.start();

        JaxRsDeployment appDeployment = new JaxRsDeployment();
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);

        ServiceDeployment deployment = new ServiceDeployment();
        deployment.addService(new MyService("/jms/topic/my-topic" ) );

        // Deploy the services
        container.deploy( deployment );

    }
}

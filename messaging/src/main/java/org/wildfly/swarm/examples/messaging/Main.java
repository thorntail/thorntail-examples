package org.wildfly.swarm.examples.messaging;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSDeployment;
import org.wildfly.swarm.messaging.MessagingFraction;
import org.wildfly.swarm.messaging.MessagingServer;
import org.wildfly.swarm.msc.ServiceActivatorDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.subsystem(new MessagingFraction()
                        .server(
                                new MessagingServer()
                                        .enableInVMConnector()
                                        .topic("my-topic")
                                        .queue("my-queue")
                        )
        );

        // Start the container
        container.start();

        JAXRSDeployment appDeployment = new JAXRSDeployment(container);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);

        ServiceActivatorDeployment deployment = new ServiceActivatorDeployment(container);
        deployment.addServiceActivator( MyServiceActivator.class );
        deployment.addClass( MyService.class );

        // Deploy the services
        container.deploy( deployment );

    }
}

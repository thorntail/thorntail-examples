package org.wildfly.swarm.examples.messaging;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.config.messaging_activemq.Server;
import org.wildfly.swarm.config.messaging_activemq.server.*;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.JARArchive;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.messaging.MessagingFraction;
import org.wildfly.swarm.messaging.MessagingServer;
import org.wildfly.swarm.msc.ServiceActivatorArchive;

import java.util.Arrays;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        final String serverName = "default";

        final ConnectionFactory connectionFactory = new ConnectionFactory("InVmConnectionFactory")
                .connectors(Arrays.asList("in-vm"))
                .entries(Arrays.asList("java:/ConnectionFactory"));

        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory("activemq-ra")
                .entries(Arrays.asList("java:jboss/DefaultJMSConnectionFactory"))
                .connectors(Arrays.asList("in-vm"))
                .transaction("xa");

        final MessagingFraction fraction = MessagingFraction.createDefaultFraction()
                .server(new Server(serverName)
                        .inVmConnector(new InVmConnector("in-vm").serverId(1))
                        .inVmAcceptor(new InVmAcceptor("in-vm").serverId(1))
                        .connectionFactory(connectionFactory)
                        .pooledConnectionFactory(pooledConnectionFactory)
                        .jmsTopic(new JmsTopic("my-topic").entries(Arrays.asList("java:/jms/topic/my-topic")))
                        .jmsQueue(new JmsQueue("my-queue").entries(Arrays.asList("java:/jms/queue/my-queue"))));


        container.subsystem(fraction);

        // Start the container
        container.start();

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);

        JARArchive deployment = ShrinkWrap.create( JARArchive.class );
        deployment.addClass(MyService.class);
        deployment.as(ServiceActivatorArchive.class).addServiceActivator(MyServiceActivator.class);

        // Deploy the services
        container.deploy(deployment);

    }
}

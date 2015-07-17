package org.wildfly.swarm.examples.jaxrs.msc;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.msc.ServiceActivatorArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        JAXRSArchive deployment = container.create("myapp.war", JAXRSArchive.class);
        deployment.addClass(MyResource.class);
        deployment.addAllDependencies();
        deployment.as(ServiceActivatorArchive.class).addServiceActivator(MyServiceActivator.class);
        deployment.addClass(MyService.class);
        container.start().deploy(deployment);
    }
}

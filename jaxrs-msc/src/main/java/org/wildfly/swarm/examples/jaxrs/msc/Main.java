package org.wildfly.swarm.examples.jaxrs.msc;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.msc.ServiceActivatorArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "myapp.war");
        deployment.addClass(MyResource.class);
        deployment.addAllDependencies();
        deployment.as(ServiceActivatorArchive.class).addServiceActivator(MyServiceActivator.class);
        deployment.addClass(MyService.class);
        container.start().deploy(deployment);
    }
}

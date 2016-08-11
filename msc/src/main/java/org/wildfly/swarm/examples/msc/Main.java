package org.wildfly.swarm.examples.msc;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.msc.ServiceActivatorArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Swarm container = new Swarm();
        container.setArgs(args);
        container.start();

        JavaArchive deployment = ShrinkWrap.create( JavaArchive.class, "myapp.jar" );

        deployment.addClass(MyService.class);
        deployment.as(ServiceActivatorArchive.class).addServiceActivator(MyServiceActivator.class);

        container.deploy(deployment);
    }
}

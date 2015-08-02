package org.wildfly.swarm.examples.msc;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.msc.ServiceActivatorArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();
        container.setArgs(args);
        container.start();

        JavaArchive deployment = ShrinkWrap.create( JavaArchive.class );

        deployment.addClass(MyService.class);
        deployment.as(ServiceActivatorArchive.class).addServiceActivator(MyServiceActivator.class);

        container.deploy(deployment);
    }
}

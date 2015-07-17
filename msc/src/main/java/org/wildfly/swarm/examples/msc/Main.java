package org.wildfly.swarm.examples.msc;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.spi.Identifiable;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.msc.ServiceActivatorArchive;
import org.wildfly.swarm.msc.ServiceActivatorDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.start();

        JavaArchive deployment = ShrinkWrap.create( JavaArchive.class );

        deployment.addClass(MyService.class);
        System.err.println( "ID: " + deployment.as(Identifiable.class).getId() );
        deployment.as(ServiceActivatorArchive.class).addServiceActivator(MyServiceActivator.class);

        container.deploy(deployment);
    }
}

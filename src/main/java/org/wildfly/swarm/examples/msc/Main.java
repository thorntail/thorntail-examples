package org.wildfly.swarm.examples.msc;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.msc.ServiceActivatorDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.start();

        ServiceActivatorDeployment deployment = new ServiceActivatorDeployment(container);

        deployment.addServiceActivator( MyServiceActivator.class );
        deployment.addClass( MyService.class );

        container.deploy( deployment );
    }
}

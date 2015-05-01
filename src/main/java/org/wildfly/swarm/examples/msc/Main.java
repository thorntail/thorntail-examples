package org.wildfly.swarm.examples.msc;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.msc.ServiceDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        // Start the container
        container.start();

        ServiceDeployment deployment = new ServiceDeployment();
        deployment.addService( new MyService("hi!" ) );
        deployment.addService( new MyService("howdy!" ) );
        container.deploy( deployment );

        deployment = new ServiceDeployment();
        deployment.addService( new MyService("hi #2!" ) );
        deployment.addService( new MyService("howdy #2!" ) );
        container.deploy( deployment );
    }
}

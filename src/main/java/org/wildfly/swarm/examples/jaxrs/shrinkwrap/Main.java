package org.wildfly.swarm.examples.jaxrs.shrinkwrap;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JaxRsDeployment;
import org.wildfly.swarm.logging.LoggingSubsystem;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        JaxRsDeployment deployment = new JaxRsDeployment();
        deployment.addResource(MyResource.class);

        container.start(deployment);
    }
}

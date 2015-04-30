package org.wildfly.swarm.examples.messaging;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JaxRsDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        // Start the container
        container.start();

        JaxRsDeployment appDeployment = new JaxRsDeployment();
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);

    }
}

package org.wildfly.swarm.examples.clustering;

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

        deployment.addServiceActivator(ClusterWatcherActivator.class);
        deployment.addClass(ClusterWatcher.class);
        deployment.addClass(PingCommand.class);

        container.deploy(deployment);
    }
}

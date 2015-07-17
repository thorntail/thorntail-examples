package org.wildfly.swarm.examples.clustering;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.JARArchive;
import org.wildfly.swarm.container.JBossDeploymentStructureContainer;
import org.wildfly.swarm.msc.ServiceActivatorArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.start();

        JARArchive deployment = ShrinkWrap.create(JARArchive.class, "clustering.jar");

        deployment.as(ServiceActivatorArchive.class).addServiceActivator(ClusterWatcherActivator.class);
        deployment.addClass(ClusterWatcher.class);
        deployment.addClass(PingCommand.class);
        deployment.addModule("org.wildfly.clustering.api" );

        container.deploy(deployment);
    }
}

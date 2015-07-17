package org.wildfly.swarm.examples.clustering;

import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.service.ServiceTarget;
import org.wildfly.clustering.dispatcher.CommandDispatcherFactory;

/**
 * @author Bob McWhirter
 */
public class ClusterWatcherActivator implements ServiceActivator {
    public void activate(ServiceActivatorContext serviceActivatorContext) throws ServiceRegistryException {

        ServiceTarget target = serviceActivatorContext.getServiceTarget();

        ClusterWatcher watcher = new ClusterWatcher();

        target.addService( ServiceName.of( "cluster", "watcher" ), watcher )
                .addDependency( ServiceName.parse( "jboss.clustering.dispatcher.default" ), CommandDispatcherFactory.class, watcher.getCommandDispatcherFactoryInjector() )
                .install();

        /*
        ClusterWatcher service = new ClusterWatcher("Hi #1");
        target.addService(ServiceName.of("my", "service", "1"), service)
                .install();

        service = new ClusterWatcher("Hi #2");
        target.addService(ServiceName.of("my", "service", "2"), service)
                .install();

        service = new ClusterWatcher("Howdy #1");
        target.addService(ServiceName.of("my", "service", "3"), service)
                .install();

        service = new ClusterWatcher("Howdy #2");
        target.addService(ServiceName.of("my", "service", "4"), service)
                .install();
        */

    }
}

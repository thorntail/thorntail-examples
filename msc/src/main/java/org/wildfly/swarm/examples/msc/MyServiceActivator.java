package org.wildfly.swarm.examples.msc;

import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.service.ServiceTarget;

/**
 * @author Bob McWhirter
 */
public class MyServiceActivator implements ServiceActivator {
    public void activate(ServiceActivatorContext serviceActivatorContext) throws ServiceRegistryException {

        ServiceTarget target = serviceActivatorContext.getServiceTarget();


        MyService service = new MyService("Hi #1");
        target.addService(ServiceName.of( "my", "service", "1"), service )
                .install();

        service = new MyService("Hi #2");
        target.addService(ServiceName.of( "my", "service", "2"), service )
                .install();

        service = new MyService("Howdy #1");
        target.addService(ServiceName.of( "my", "service", "3"), service )
                .install();

        service = new MyService("Howdy #2");
        target.addService(ServiceName.of( "my", "service", "4"), service )
                .install();

    }
}

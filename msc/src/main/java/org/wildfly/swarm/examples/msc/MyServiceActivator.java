package org.wildfly.swarm.examples.msc;

import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.ValueService;

import java.util.Arrays;

/**
 * @author Bob McWhirter
 */
public class MyServiceActivator implements ServiceActivator {
    public void activate(ServiceActivatorContext serviceActivatorContext) throws ServiceRegistryException {

        ServiceTarget target = serviceActivatorContext.getServiceTarget();
        // Get the command args passed to main, not currently used by MyService
        ServiceController argsController = serviceActivatorContext.getServiceRegistry().getService(ServiceName.of("wildfly", "swarm", "main-args"));
        ValueService<String[]> argsService =  (ValueService<String[]>) argsController.getService();
        String[] args = argsService.getValue();
        System.err.printf("Args available to services: %s\n", Arrays.asList(args));

        MyService service = new MyService("Hi #1");
        target.addService(ServiceName.of("my", "service", "1"), service)
                .install();

        service = new MyService("Hi #2");
        target.addService(ServiceName.of("my", "service", "2"), service)
                .install();

        service = new MyService("Howdy #1");
        target.addService(ServiceName.of("my", "service", "3"), service)
                .install();

        service = new MyService("Howdy #2");
        target.addService(ServiceName.of("my", "service", "4"), service)
                .install();

    }
}

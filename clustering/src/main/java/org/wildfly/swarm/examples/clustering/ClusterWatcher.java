package org.wildfly.swarm.examples.clustering;

import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.dispatcher.CommandDispatcher;
import org.wildfly.clustering.dispatcher.CommandDispatcherFactory;
import org.wildfly.clustering.dispatcher.CommandResponse;
import org.wildfly.clustering.group.Node;

import java.util.List;
import java.util.Map;

/**
 * @author Bob McWhirter
 */
public class ClusterWatcher implements Service<Void> {

    private Thread thread;

    private boolean shouldRun;

    private InjectedValue<CommandDispatcherFactory> dispatcherInjector = new InjectedValue();

    public ClusterWatcher() {
    }

    public void start(final StartContext startContext) throws StartException {

        CommandDispatcherFactory factory = this.dispatcherInjector.getValue();
        factory.getGroup().addListener((List<Node> previousMembers, List<Node> members, boolean merged) -> {
            System.err.println("Membership now: " + members);
        });
        CommandDispatcher<Node> dispatcher = factory.createCommandDispatcher("ping", factory.getGroup().getLocalNode());

        String nodeName = System.getProperty( "jboss.node.name" );
        startContext.asynchronous();
        this.shouldRun = true;
        this.thread = new Thread(() -> {
            startContext.complete();

            while (shouldRun) {
                try {
                    Thread.sleep(1000);
                    if (factory.getGroup().getNodes().isEmpty()) {
                        //continue;
                    }
                    Map<Node, CommandResponse<Object>> result = dispatcher.executeOnCluster(new PingCommand(nodeName), factory.getGroup().getLocalNode());
                    for (CommandResponse each : result.values()) {
                        System.err.println(" Responses from PING -> " + each.get());
                    }
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        this.thread.start();
    }

    public void stop(StopContext stopContext) {
        this.shouldRun = false;
    }

    public Void getValue() throws IllegalStateException, IllegalArgumentException {
        return null;
    }

    public Injector<CommandDispatcherFactory> getCommandDispatcherFactoryInjector() {
        return this.dispatcherInjector;
    }

}


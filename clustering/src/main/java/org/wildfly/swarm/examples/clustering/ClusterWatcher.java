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
import org.wildfly.clustering.group.Group;
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
        CommandDispatcher<Node> dispatcher = factory.createCommandDispatcher("ping", factory.getGroup().getLocalNode());
        System.err.println("I am " + factory.getGroup().getLocalNode().getSocketAddress());

        factory.getGroup().addListener((List<Node> previousMembers, List<Node> members, boolean merged) -> {
            System.err.println("Membership now: " + members);
        });

        String requester = factory.getGroup().getLocalNode().getSocketAddress().toString();
        startContext.asynchronous();
        this.shouldRun = true;
        this.thread = new Thread(() -> {
            startContext.complete();

            while (shouldRun) {
                try {
                    Thread.sleep(1000);
                    if (factory.getGroup().getNodes().isEmpty()) {
                        System.err.println("no nodes");
                        //continue;
                    }
                    System.err.println("issue command " + factory.getGroup().getNodes());
                    Map<Node, CommandResponse<Object>> result = dispatcher.executeOnCluster(new PingCommand(requester), factory.getGroup().getLocalNode());
                    for (CommandResponse each : result.values()) {
                        System.err.println(" -> " + each.get());
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


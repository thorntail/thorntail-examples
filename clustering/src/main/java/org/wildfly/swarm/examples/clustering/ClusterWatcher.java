package org.wildfly.swarm.examples.clustering;

import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.dispatcher.CommandDispatcher;
import org.wildfly.clustering.dispatcher.CommandDispatcherFactory;
import org.wildfly.clustering.group.Group;
import org.wildfly.clustering.group.Node;

import java.util.List;

/**
 * @author Bob McWhirter
 */
public class ClusterWatcher implements Service<Void> {

    private Thread thread;

    private boolean shouldRun;

    private InjectedValue dispatcherInjector = new InjectedValue();
    private InjectedValue groupInjector = new InjectedValue();
    private InjectedValue nodesInjector = new InjectedValue();

    public ClusterWatcher() {
    }

    public void start(final StartContext startContext) throws StartException {
        System.err.println(" dispatcher ---> " + this.dispatcherInjector.getValue());
        System.err.println(" group ---> " + this.groupInjector.getValue());
        System.err.println(" nodes ---> " + this.nodesInjector.getValue());


        CommandDispatcherFactory factory = (CommandDispatcherFactory) this.dispatcherInjector.getValue();
        CommandDispatcher<Node> dispatcher = factory.createCommandDispatcher("ping", factory.getGroup().getLocalNode());
        System.err.println( "I am " + factory.getGroup().getLocalNode().getSocketAddress() );

        factory.getGroup().addListener(new Group.Listener() {
            @Override
            public void membershipChanged(List<Node> previousMembers, List<Node> members, boolean merged) {
                System.err.println( "Membership now: " + members );
            }
        });

        String requester = factory.getGroup().getLocalNode().getSocketAddress().toString();
        startContext.asynchronous();
        this.shouldRun = true;
        this.thread = new Thread(new Runnable() {
            public void run() {
                startContext.complete();

                while (shouldRun) {
                    try {
                        Thread.sleep(1000);
                        dispatcher.executeOnCluster( new PingCommand( requester ) );
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public Injector getDispatcherInjector() {
        return this.dispatcherInjector;
    }

    public Injector getGroupInjector() {
        return this.groupInjector;
    }

    public Injector getNodesInjector() {
        return this.nodesInjector;
    }
}


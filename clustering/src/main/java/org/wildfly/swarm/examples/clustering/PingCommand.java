package org.wildfly.swarm.examples.clustering;

import org.wildfly.clustering.dispatcher.Command;
import org.wildfly.clustering.group.Node;

import java.io.Serializable;

/**
 * @author Bob McWhirter
 */
public class PingCommand implements Command<Object, Node>, Serializable {

    public static final Long serialVersionUID = 1L;

    private final String requester;

    public PingCommand(String requester) {
        this.requester = requester;
    }

    @Override
    public Object execute(Node node) throws Exception {
        System.err.println( "HI from " + requester );
        //System.err.println(this.requester + " :: running against :: " + node.getSocketAddress());
        return node.getSocketAddress().toString() + " says hi in return";
    }
}

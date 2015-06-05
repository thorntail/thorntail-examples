package org.wildfly.swarm.examples.staticcontent;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.undertow.StaticDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        StaticDeployment deployment = new StaticDeployment(container, "/static");

        container.start().deploy(deployment);

    }
}

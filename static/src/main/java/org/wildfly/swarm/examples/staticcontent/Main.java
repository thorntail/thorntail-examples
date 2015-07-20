package org.wildfly.swarm.examples.staticcontent;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        WARArchive deployment = ShrinkWrap.create( WARArchive.class );

        deployment.staticContent("/static");

        container.start().deploy(deployment);

    }
}

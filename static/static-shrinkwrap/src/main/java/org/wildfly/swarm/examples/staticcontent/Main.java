package org.wildfly.swarm.examples.staticcontent;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Swarm swarm = new Swarm();

        WARArchive deployment = ShrinkWrap.create( WARArchive.class );

        deployment.staticContent();

        swarm.start().deploy(deployment);

    }
}

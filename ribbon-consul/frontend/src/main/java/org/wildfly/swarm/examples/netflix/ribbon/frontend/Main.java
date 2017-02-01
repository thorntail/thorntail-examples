package org.wildfly.swarm.examples.netflix.ribbon.frontend;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * @author Lance Ball
 */
public class Main {
    public static void main(String... args) throws Exception {
        Swarm swarm = new Swarm();
        swarm.start();
        WARArchive war = ShrinkWrap.create(WARArchive.class);
        war.staticContent();
        war.addAllDependencies();
        swarm.deploy(war);
    }
}

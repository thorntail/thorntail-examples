package org.wildfly.swarm.examples.netflix.ribbon.events;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Swarm container = new Swarm();
        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage( Main.class.getPackage() );
        deployment.addAllDependencies();
        //deployment.as( RibbonArchive.class );
        container.start().deploy(deployment);
    }
}

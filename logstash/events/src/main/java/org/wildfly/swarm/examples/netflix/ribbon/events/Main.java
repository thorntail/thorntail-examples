package org.wildfly.swarm.examples.netflix.ribbon.events;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.netflix.ribbon.RibbonArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();
        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage( Main.class.getPackage() );
        deployment.addAllDependencies();
        //deployment.as( RibbonArchive.class );
        container.start().deploy(deployment);
    }
}

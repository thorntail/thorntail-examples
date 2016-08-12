package org.wildfly.swarm.examples.netflix.ribbon.time;

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
        deployment.addResource(TimeResource.class);
        deployment.addAllDependencies();
        deployment.as(RibbonArchive.class).setApplicationName("time");
        container.start().deploy(deployment);
    }
}

package org.wildfly.swarm.examples.multi.time;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();
        JAXRSArchive deployment = ShrinkWrap.create( JAXRSArchive.class );
        deployment.addResource(TimeResource.class);
        container.start().deploy(deployment);
    }
}

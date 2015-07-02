package org.wildfly.swarm.examples.gradle;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSDeployment;
import org.wildfly.swarm.undertow.WarDeployment;

/**
 * @author helio frota
 *
 */
public class Main {

    public static void main(String... args) throws Exception {

        Container container = new Container();

        WarDeployment war = new JAXRSDeployment(container);
        war.staticContent("/");
        war.getArchive().addClass(HelloRest.class);
        container.start().deploy(war);

    }
}

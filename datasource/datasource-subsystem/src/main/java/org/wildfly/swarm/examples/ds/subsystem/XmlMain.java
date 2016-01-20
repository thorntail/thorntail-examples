package org.wildfly.swarm.examples.ds.subsystem;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import java.net.URL;

/**
 * @author Heiko Braun
 * @since 10/11/15
 */
public class XmlMain {

    public static void main(String[] args) throws Exception {

        ClassLoader cl = XmlMain.class.getClassLoader();
        URL xmlConfig = cl.getResource("standalone.xml");

        assert xmlConfig!=null : "Failed to load standalone.xml";

        Container container = new Container(false, xmlConfig);

        //container.fraction(new LoggingFraction());
        container.fraction(new DatasourcesFraction());

        // Start the container
        container.start();

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);


    }
}

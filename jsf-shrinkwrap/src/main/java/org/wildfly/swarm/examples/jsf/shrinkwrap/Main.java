package org.wildfly.swarm.examples.jsf.shrinkwrap;

import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.undertow.DefaultWarDeployment;
import org.wildfly.swarm.undertow.WarDeployment;

/**
 * @author Yoshimasa Tanabe
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        WarDeployment deployment = new DefaultWarDeployment(container);

        deployment.getArchive().addClass(Message.class);

        deployment.getArchive().addAsWebResource(
                new ClassLoaderAsset("index.html", Main.class.getClassLoader()), "index.html");
        deployment.getArchive().addAsWebResource(
                new ClassLoaderAsset("index.xhtml", Main.class.getClassLoader()), "index.xhtml");

        deployment.getArchive().addAsWebInfResource(
                new ClassLoaderAsset("WEB-INF/web.xml", Main.class.getClassLoader()), "web.xml");
        deployment.getArchive().addAsWebInfResource(
                new ClassLoaderAsset("WEB-INF/template.xhtml", Main.class.getClassLoader()), "template.xhtml");

        container.start().deploy(deployment);

    }
}

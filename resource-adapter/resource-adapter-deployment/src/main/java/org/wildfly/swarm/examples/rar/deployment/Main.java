package org.wildfly.swarm.examples.rar.deployment;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.ejb.EJBFraction;
import org.wildfly.swarm.resource.adapters.RARArchive;

/**
 * @author Ralf Battenfeld
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final ClassLoader classLoader = Main.class.getClassLoader();
        final File raFile = new File(classLoader.getResource("ra.xml").getFile());
        final File ironJacamarFile = new File(classLoader.getResource("ironjacamar.xml").getFile());
        final Container container = new Container();
        container.fraction(EJBFraction.createDefaultFraction());
        container.start();
        
        // since Swarm.artifact doesn't support rar archives, we have to manually create a rar
        final RARArchive rarArchive = ShrinkWrap.create(RARArchive.class, "xadisk.rar");
        rarArchive.addAsLibrary(Swarm.artifact("net.java.xadisk:xadisk:jar:1.2.2"));
        rarArchive.addAsManifestResource(raFile, "ra.xml");
        
        // add the required ironjacamar.xml descriptor
        rarArchive.resourceAdapter(ironJacamarFile);
        container.deploy(rarArchive);

        final JavaArchive appDeployment = ShrinkWrap.create(JavaArchive.class);
        appDeployment.merge(Swarm.artifact("net.java.xadisk:xadisk:jar:1.2.2"));
        appDeployment.addClass(FileIOBean.class);

        // Deploy your app
        container.deploy(appDeployment);
    }
}

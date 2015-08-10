package org.wildfly.swarm.examples.simple;

import java.util.Iterator;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.Resource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.JARArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        Module app = Module.getBootModuleLoader().loadModule(ModuleIdentifier.create("swarm.application"));
        Iterator<Resource> resources = app.getClassLoader().iterateResources("", true);

        System.err.println( ">>>" );
        while ( resources.hasNext() ) {
            Resource each = resources.next();
            System.err.println( "resource: " + each.getName() );
        }
        System.err.println("<<<");

        JARArchive deployment = ShrinkWrap.create(JARArchive.class);
        System.err.println( "--> " + deployment + " // " + deployment.getClass() );
        Iterable<ClassLoader> classloaders = ShrinkWrap.getDefaultDomain().getConfiguration().getClassLoaders();
        for (ClassLoader classloader : classloaders) {
            System.err.println( "load Shrinkwrap bits from: " + classloader );
        }

        deployment.add(EmptyAsset.INSTANCE, "/nothing");
        container.start().deploy( deployment );
    }
}

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

        JARArchive deployment = ShrinkWrap.create(JARArchive.class);

        deployment.add(EmptyAsset.INSTANCE, "/nothing");
        container.start().deploy( deployment );
    }
}

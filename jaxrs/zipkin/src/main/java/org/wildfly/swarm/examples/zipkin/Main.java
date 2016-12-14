package org.wildfly.swarm.examples.zipkin;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jaxrs.btm.ZipkinFraction;

/**
 * @author Pavol Loffay
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Swarm container = new Swarm();

        ZipkinFraction zipkinFraction = new ZipkinFraction("wildfly-swarm");
        zipkinFraction.reportAsync("http://localhost:9411/api/v1/spans")
                .sampleRate(1.0f);

        container.fraction(zipkinFraction);

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage( Main.class.getPackage() );
        deployment.addAllDependencies();

        container.start().deploy(deployment);
    }
}

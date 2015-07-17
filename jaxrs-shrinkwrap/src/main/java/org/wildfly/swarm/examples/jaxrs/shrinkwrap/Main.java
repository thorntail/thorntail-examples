package org.wildfly.swarm.examples.jaxrs.shrinkwrap;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        JAXRSArchive archive = container.create( "myapp.war", JAXRSArchive.class );
        archive.addResource(MyResource.class);
        archive.addClass( MyApp.class );
        archive.addClass( MyApp.class.getName() );
        archive.addModule( "com.foo.bar", "main" );
        archive.addModule( "com.foo.bar", "taco" );
        container.start().deploy( archive );

        /*

        JAXRSDeployment deployment = new JAXRSDeployment(container);
        deployment.addResource(MyResource.class);

        System.err.println( javax.security.auth.login.LoginException.class );

        container.start().deploy(deployment);
        */


    }
}

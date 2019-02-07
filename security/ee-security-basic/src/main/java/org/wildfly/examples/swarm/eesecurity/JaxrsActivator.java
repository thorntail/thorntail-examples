package org.wildfly.examples.swarm.eesecurity;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * A javax.ws.rs.core.Application subclass to activate Jaxrs capabilities.
 */
@ApplicationPath("/")
public class JaxrsActivator extends Application {

    public JaxrsActivator() {
    }
}

package org.wildfly.swarm.examples.jaxrs;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Heiko Braun
 */
@Path("/")
public class MyResource {

    @POST
    @Produces("text/plain")
    public String getName(@HeaderParam("client-name") String name) {
        return "Howdy " + name;
    }
}

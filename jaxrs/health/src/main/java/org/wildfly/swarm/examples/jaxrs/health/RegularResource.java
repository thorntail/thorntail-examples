package org.wildfly.swarm.examples.jaxrs.health;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/app")
public class RegularResource {

    @GET
    @Path("/time")
    @Produces(MediaType.TEXT_PLAIN)
    public String time() {
        return String.valueOf(System.currentTimeMillis());
    }

}

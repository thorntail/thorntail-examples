package org.wildfly.swarm.examples.kotlin

import javax.ws.rs.Path
import javax.ws.rs.GET

// @author Helio Frota
@Path("/")
class KotlinResource {

    @GET
    public fun get() : String {
        return "Hail at " + java.util.Date()
    }

}

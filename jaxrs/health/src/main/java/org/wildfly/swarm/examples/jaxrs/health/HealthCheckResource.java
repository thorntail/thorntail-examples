package org.wildfly.swarm.examples.jaxrs.health;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wildfly.swarm.monitor.Health;

@Path("/app")
public class HealthCheckResource {

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    @Health
    public String firstHealthCheckMethod() {
        return "Healthy: On HealthCheckResource#firstHealthCheckMethod()";
    }

    @GET
    @Path("/second-health")
    @Produces(MediaType.TEXT_PLAIN)
    @Health(inheritSecurity = false)
    public String secondHealthCheckMethod() {
        return "Healthy: On HealthCheckResource#secondHealthCheckMethod()";
    }

}

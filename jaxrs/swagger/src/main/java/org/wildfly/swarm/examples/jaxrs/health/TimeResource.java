package org.wildfly.swarm.examples.jaxrs.health;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import io.swagger.annotations.*;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;

@Path("/time")
@Api(value = "/time", description = "Get the time", tags = "time")
@Produces(MediaType.APPLICATION_JSON)
public class TimeResource {

    @GET
    @Path("/now")
    @ApiOperation(value = "Get the current time",
            notes = "Returns the time as a string",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return String.format("{\"value\" : \"The time is %s\"}", new DateTime());
    }
}

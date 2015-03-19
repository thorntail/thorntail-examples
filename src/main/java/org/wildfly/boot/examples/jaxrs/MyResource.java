package org.wildfly.boot.examples.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Bob McWhirter
 */
@Path( "/" )
public class MyResource {

    @GET
    @Produces( "text/plain" )
    public String get() {
        return "Howdy!";
    }
}

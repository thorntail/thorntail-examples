package org.wildfly.swarm.examples.servlet;

import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("")
public class FrontendService {

    @GET
    @Path("{path:.*}")
    public InputStream getResourceStream(@PathParam("path") String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(
            path.isEmpty() ? "index.html" : path);
    }
}

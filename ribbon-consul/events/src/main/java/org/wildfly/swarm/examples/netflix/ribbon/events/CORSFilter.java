package org.wildfly.swarm.examples.netflix.ribbon.events;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * @author Lance Ball
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {
    public void filter(ContainerRequestContext paramContainerRequestContext,
                       ContainerResponseContext paramContainerResponseContext)
            throws IOException {
        paramContainerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        paramContainerResponseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        paramContainerResponseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        paramContainerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        paramContainerResponseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
    }
}

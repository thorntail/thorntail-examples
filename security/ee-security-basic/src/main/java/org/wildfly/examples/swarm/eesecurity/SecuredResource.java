package org.wildfly.examples.swarm.eesecurity;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Sample security-context-aware rest endpoint. Shows how to access authenticated principal information.
 */
@Path("/whoami")
public class SecuredResource {

    @Inject
    private SecurityContext sc;

    @GET
    @Produces("application/json")
    public Map<String, Object> get() {
        HashMap<String, Object> response = new HashMap<>();

        if (sc.getCallerPrincipal() != null) {
            response.put("logged in", true);
            response.put("caller", sc.getCallerPrincipal().getName());
            response.put("has role user", sc.isCallerInRole("user"));
            response.put("has role admin", sc.isCallerInRole("admin"));
        } else {
            response.put("logged in", false);
        }

        return response;
    }
}

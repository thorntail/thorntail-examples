package org.wildfly.swarm.examples.jaxrs.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wildfly.swarm.cdi.ConfigValue;
import org.wildfly.swarm.cdi.Configured;

/**
 * @author Heiko Braun
 */
@ApplicationScoped
@Configured
@Path("/app")
public class Controller {

    @ConfigValue("some.string.property")
    String stringProperty;

    @ConfigValue("some.integer.property")
    Integer intProperty;

    @ConfigValue("some.boolean.property")
    Boolean boolProperty;

    @GET
    @Path("/propertyString")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStringProperty() {
        if(null==stringProperty) throw new RuntimeException("config property not initialised");
        return stringProperty;
    }

    @GET
    @Path("/propertyInteger")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIntegerProperty() {
        if(null==intProperty) throw new RuntimeException("config property not initialised");
        return String.valueOf(intProperty);
    }

    @GET
    @Path("/propertyBoolean")
    @Produces(MediaType.TEXT_PLAIN)
    public String getBoolProperty() {
        if(null==boolProperty) throw new RuntimeException("config property not initialised");
        return String.valueOf(boolProperty);
    }
}

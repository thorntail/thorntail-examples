package org.wildfly.swarm.examples.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Ken Finnigan
 */
@Path("/")
@ApplicationScoped
public class EmployeeResource {

    @Inject
    EntityManager em;

    @GET
    @Produces("application/json")
    public Employee[] get() {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList().toArray(new Employee[0]);
    }
}

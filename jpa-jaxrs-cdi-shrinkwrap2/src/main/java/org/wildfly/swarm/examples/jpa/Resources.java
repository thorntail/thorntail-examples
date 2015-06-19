package org.wildfly.swarm.examples.jpa;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Bob McWhirter
 */
public class Resources {

    @Produces
    @PersistenceContext
    private EntityManager em;

}

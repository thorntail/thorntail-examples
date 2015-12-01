package org.wildfly.swarm.examples.servlet.cdi;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Ken Finnigan
 */
@ApplicationScoped
public class Greeter {
    public String greet() {
        return "Howdy at " + LocalDateTime.now();
    }
}

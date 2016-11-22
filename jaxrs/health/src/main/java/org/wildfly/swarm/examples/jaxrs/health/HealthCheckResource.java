package org.wildfly.swarm.examples.jaxrs.health;

import java.io.File;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.wildfly.swarm.monitor.Health;
import org.wildfly.swarm.monitor.HealthStatus;

@Path("/app")
public class HealthCheckResource {

    @GET
    @Path("/health")
    @Health
    public HealthStatus checkDiskspace() {

          File path = new File(System.getProperty("user.home"));
        long freeBytes = path.getFreeSpace();
        long threshold = 1024 * 1024 * 100; // 100mb
        return freeBytes>threshold ?
                HealthStatus.
                        named("diskspace")
                        .up()
                        .withAttribute("freebytes", freeBytes) :
                HealthStatus.
                        named("diskspace")
                        .down()
                        .withAttribute("freebytes", freeBytes);
    }

    @GET
    @Path("/second-health")
    @Health
    public HealthStatus checkSomethingElse() {

        int tipping = (int) Math.ceil(Math.random() * 100);

        if(tipping>50) {
            return HealthStatus
                    .named("something-else")
                    .up()
                    .withAttribute("date", new Date().toString())
                    .withAttribute("time", System.currentTimeMillis());
        } else {

            return HealthStatus
                    .named("seomthing-else")
                    .down();
        }
    }

}

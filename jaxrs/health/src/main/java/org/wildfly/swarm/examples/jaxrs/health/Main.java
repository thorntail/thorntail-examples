package org.wildfly.swarm.examples.jaxrs.health;

import java.util.Properties;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.management.ManagementFraction;
import org.wildfly.swarm.monitor.MonitorFraction;

/**
 * @author Lance Ball
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Swarm swarm = new Swarm();

        JAXRSArchive archive = ShrinkWrap.create(JAXRSArchive.class, "healthcheck-app.war");
        JAXRSArchive deployment = archive.as(JAXRSArchive.class).addPackage(Main.class.getPackage());
        deployment.addResource(HealthCheckResource.class);
        deployment.addResource(RegularResource.class);

        deployment.addAllDependencies();
        swarm
                .fraction(LoggingFraction.createDefaultLoggingFraction())
                .fraction(new MonitorFraction().securityRealm("ManagementRealm"))
                .fraction(ManagementFraction
                        .createDefaultFraction()
                                  .httpInterfaceManagementInterface()
                                  .securityRealm("ManagementRealm", (realm) -> {
                                      realm.inMemoryAuthentication((authn) -> {
                                          authn.add(new Properties() {{
                                              put("admin", "password");
                                          }}, true);
                                      });
                                      realm.inMemoryAuthorization();
                        }))
                .start()
                .deploy(deployment);
    }
}

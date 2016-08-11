package org.wildfly.swarm.examples.config.projectStage;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author Heiko Braun
 * @since 10/11/15
 */
public class ProjectStagesMain {

    public static void main(String[] args) throws Exception {
        Swarm swarm = new Swarm(args);

        System.err.println("Connection URL: " + swarm.stageConfig().resolve("database.connection.url").getValue());

        swarm.fraction(
                new DatasourcesFraction()
                        .jdbcDriver("h2", (d) -> {
                            d.driverClassName("org.h2.Driver");
                            d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                            d.driverModuleName("com.h2database.h2");
                        })
                        .dataSource("ExampleDS", (ds) -> {

                            ds.driverName("h2");

                            ds.connectionUrl(
                                    swarm
                                            .stageConfig()
                                            .resolve("database.connection.url")
                                            .getValue()
                            );
                            ds.userName("sa");
                            ds.password("sa");
                        })
        );

        // Start the container
        swarm.start();

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        swarm.deploy(appDeployment);
    }
}

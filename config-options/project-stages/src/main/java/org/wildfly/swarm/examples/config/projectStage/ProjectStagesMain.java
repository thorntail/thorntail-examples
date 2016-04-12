package org.wildfly.swarm.examples.config.projectStage;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.config.datasources.DataSource;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import java.net.URL;

/**
 * @author Heiko Braun
 * @since 10/11/15
 */
public class ProjectStagesMain {

    public static void main(String[] args) throws Exception {

        ClassLoader cl = ProjectStagesMain.class.getClassLoader();
        URL stageConfig = cl.getResource("project-stages.yml");

        assert stageConfig!=null : "Failed to load stage configuration";

        Container container = new Container(false)
                .withStageConfig(stageConfig);

        container.fraction(
                new DatasourcesFraction()
                        .jdbcDriver("h2", (d) -> {
                            d.driverClassName("org.h2.Driver");
                            d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                            d.driverModuleName("com.h2database.h2");
                        })
                        .dataSource("ExampleDS", (ds) -> {

                            ds.driverName("h2");

                            ds.connectionUrl(
                                    container
                                            .stageConfig()
                                            .resolve("database.connection.url")
                                            .getValue()
                            );
                            ds.userName("sa");
                            ds.password("sa");
                        })
        );

        // Start the container
        container.start();

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);


    }
}

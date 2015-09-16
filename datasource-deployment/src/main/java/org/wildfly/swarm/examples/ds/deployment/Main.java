package org.wildfly.swarm.examples.ds.deployment;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.ArtifactManager;
import org.wildfly.swarm.config.datasources.subsystem.dataSource.DataSource;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.JARArchive;
import org.wildfly.swarm.datasources.DatasourceArchive;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        // Start the container
        container.start();

        // Create a JDBC driver deployment using maven groupId:artifactId
        // The version is resolved from your pom.xml's <dependency>
        container.deploy(ArtifactManager.artifact("com.h2database:h2", "h2"));

        /*
        // Create a DS deployment
        DatasourceDeployment dsDeployment = new DatasourceDeployment(container, new Datasource("ExampleDS")
                .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                .driver("h2")
                .authentication("sa", "sa")
        );
        */

        JARArchive dsArchive = ShrinkWrap.create(JARArchive.class);
        dsArchive.as(DatasourceArchive.class).datasource(
                new DataSource("ExampleDS")
                        .connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                        .driverName("h2")
                        .userName("sa")
                        .password("sa")
        );

        // Deploy the datasource
        container.deploy(dsArchive);

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);

    }
}

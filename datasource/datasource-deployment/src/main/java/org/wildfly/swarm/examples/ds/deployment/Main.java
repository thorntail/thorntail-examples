package org.wildfly.swarm.examples.ds.deployment;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourceArchive;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Swarm swarm = new Swarm();

        swarm.start();

        // Create a JDBC driver deployment using maven groupId:artifactId
        // The version is resolved from your pom.xml's <dependency>
        swarm.deploy(Swarm.artifact("com.h2database:h2", "h2"));

        // Create a DS deployment
        DatasourceArchive dsArchive = ShrinkWrap.create(DatasourceArchive.class);
        dsArchive.dataSource("ExampleDS", (ds) -> {
            ds.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            ds.driverName("h2");
            ds.userName("sa");
            ds.password("sa");
        });

        // Deploy the datasource
        swarm.deploy(dsArchive);

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        swarm.deploy(appDeployment);

    }
}

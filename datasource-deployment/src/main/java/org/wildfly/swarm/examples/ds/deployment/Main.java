package org.wildfly.swarm.examples.ds.deployment;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.Datasource;
import org.wildfly.swarm.datasources.DatasourceDeployment;
import org.wildfly.swarm.datasources.DriverDeployment;
import org.wildfly.swarm.jaxrs.JAXRSDeployment;

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
        DriverDeployment driverDeployment = new DriverDeployment(container, "com.h2database:h2", "h2");

        // Deploy the JDBC driver
        container.deploy(driverDeployment);

        // Create a DS deployment
        DatasourceDeployment dsDeployment = new DatasourceDeployment(container, new Datasource("ExampleDS")
                .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                .driver("h2")
                .authentication("sa", "sa")
        );

        // Deploy the datasource
        container.deploy(dsDeployment);

        JAXRSDeployment appDeployment = new JAXRSDeployment(container);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);

    }
}

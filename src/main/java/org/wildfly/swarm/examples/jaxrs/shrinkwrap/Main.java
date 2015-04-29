package org.wildfly.swarm.examples.jaxrs.shrinkwrap;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.Datasource;
import org.wildfly.swarm.datasources.DatasourceDeployment;
import org.wildfly.swarm.datasources.DriverDeployment;
import org.wildfly.swarm.jaxrs.JaxRsDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        /*

        ** You can deploy a driver/datasource via a Subsystem, if
        ** you somehow arrange for the driver module to appear
        ** in the modules/ directory, which is sub-optimal but
        ** does indeed work for h2 at least

        container.subsystem(new DatasourcesFraction()
                        .driver(new Driver("h2")
                                .xaDatasourceClassName("org.h2.jdbcx.JdbcDataSource")
                                .module("com.h2database.h2"))
                        .datasource(new Datasource("ExampleDS")
                                .driver("h2")
                                .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                                .authentication("sa", "sa"))
        );
        */


        Container container = new Container();

        JaxRsDeployment appDeployment = new JaxRsDeployment();
        appDeployment.addResource(MyResource.class);

        // Create a JDBC driver deployment using maven groupId:artifactId
        // The version is resolved from your pom.xml's <dependency>
        DriverDeployment driverDeployment = new DriverDeployment( "com.h2database:h2", "h2" );

        // Create a DS deployment
        DatasourceDeployment dsDeployment = new DatasourceDeployment(new Datasource("ExampleDS")
                .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                .driver("h2" )
                .authentication("sa", "sa")
        );

        // Start the container
        container.start();

        // Deploy the JDBC driver
        container.deploy(driverDeployment);

        // Deploy the datasource
        container.deploy( dsDeployment );

        // Deploy your app
        container.deploy( appDeployment );

    }
}

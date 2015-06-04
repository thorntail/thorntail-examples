package org.wildfly.swarm.examples.ds.subsystem;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.Datasource;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.datasources.Driver;
import org.wildfly.swarm.jaxrs.JAXRSDeployment;

import java.io.PrintStream;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        // Configure the Datasources subsystem with a driver
        // and a datasource
        container.subsystem(new DatasourcesFraction()
                        .driver(new Driver("h2")
                                .datasourceClassName( "org.h2.Driver" )
                                .xaDatasourceClassName("org.h2.jdbcx.JdbcDataSource")
                                .module("com.h2database.h2"))
                        .datasource(new Datasource("ExampleDS")
                                .driver("h2")
                                .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                                .authentication("sa", "sa"))
        );


        // Start the container
        container.start();
        JAXRSDeployment appDeployment = new JAXRSDeployment(container);
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);

    }
}

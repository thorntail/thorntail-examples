package org.wildfly.swarm.examples.ds.subsystem;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.config.datasources.subsystem.dataSource.DataSource;
import org.wildfly.swarm.config.datasources.subsystem.jdbcDriver.JdbcDriver;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.Fraction;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Container container = new Container();

        // Configure the Datasources subsystem with a driver
        // and a datasource
        container.subsystem((Fraction) new DatasourcesFraction()
                        .jdbcDriver(new JdbcDriver("h2")
                                .driverDatasourceClassName("org.h2.Driver")
                                .xaDatasourceClass("org.h2.jdbcx.JdbcDataSource")
                                .driverModuleName("com.h2database.h2"))
                        .dataSource(new DataSource("ExampleDS")
                                .driverName("h2")
                                .connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                                .userName("sa")
                                .password("sa"))
        );


        // Start the container
        container.start();
        JAXRSArchive appDeployment = ShrinkWrap.create( JAXRSArchive.class );
        appDeployment.addResource(MyResource.class);

        // Deploy your app
        container.deploy(appDeployment);

    }
}

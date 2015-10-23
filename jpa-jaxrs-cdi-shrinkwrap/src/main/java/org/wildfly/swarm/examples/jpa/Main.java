package org.wildfly.swarm.examples.jpa;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.config.datasources.DataSource;
import org.wildfly.swarm.datasources.JdbcDriver;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;

/**
 * @author Ken Finnigan
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.fraction(new DatasourcesFraction()
                        .jdbcDriver(new JdbcDriver("h2")
                                .driverName("h2")
                                .driverDatasourceClassName("org.h2.Driver")
                                .xaDatasourceClass("org.h2.jdbcx.JdbcDataSource")
                                .driverModuleName("com.h2database.h2"))
                        .dataSource(new DataSource("MyDS")
                                .driverName("h2")
                                .connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                                .userName("sa")
                                .password("sa"))
        );

        // Prevent JPA Fraction from installing it's default datasource fraction
        container.fraction(new JPAFraction()
                        .inhibitDefaultDatasource()
                        .defaultDatasource("jboss/datasources/MyDS")
        );

        container.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addClasses(Employee.class);
        deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");
        deployment.addResource(EmployeeResource.class);
        deployment.addAllDependencies();

        container.deploy(deployment);
    }
}

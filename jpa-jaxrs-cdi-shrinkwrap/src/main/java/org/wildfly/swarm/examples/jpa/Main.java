package org.wildfly.swarm.examples.jpa;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
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
                        .jdbcDriver("h2", (d) -> {
                            d.driverDatasourceClassName("org.h2.Driver");
                            d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                            d.driverModuleName("com.h2database.h2");
                        })
                        .dataSource("MyDS", (ds) -> {
                            ds.driverName("h2");
                            ds.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
                            ds.userName("sa");
                            ds.password("sa");
                        })
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

package org.wildfly.swarm.examples.jpa;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.wildfly.swarm.config.datasources.subsystem.dataSource.DataSource;
import org.wildfly.swarm.config.datasources.subsystem.jdbcDriver.JdbcDriver;
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

        container.subsystem(new DatasourcesFraction()
                        .jdbcDriver(new JdbcDriver("h2")
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
                        .defaultDatasourceName("MyDS")
        );

        //container.fraction(LoggingFraction.createTraceLoggingFraction());

        container.start();

        JAXRSArchive deployment = ShrinkWrap.create( JAXRSArchive.class );
        deployment.addClass(Employee.class);
        deployment.addClass(Resources.class);
        deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");

        deployment.addAsWebInfResource(new StringAsset("<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"\n" +
                "        http://xmlns.jcp.org/xml/ns/javaee\n" +
                "        http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\" bean-discovery-mode=\"all\">\n" +
                "</beans>"), "beans.xml");
        deployment.addResource(EmployeeResource.class);
        deployment.addAllDependencies();

        container.deploy(deployment);
    }
}

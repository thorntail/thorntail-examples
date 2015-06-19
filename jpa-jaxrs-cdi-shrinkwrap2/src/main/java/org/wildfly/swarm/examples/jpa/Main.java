package org.wildfly.swarm.examples.jpa;

import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.Datasource;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.datasources.Driver;
import org.wildfly.swarm.jaxrs.JAXRSDeployment;
import org.wildfly.swarm.jpa.JPAFraction;

/**
 * @author Ken Finnigan
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.subsystem(new DatasourcesFraction()
                        .driver(new Driver("h2")
                                .datasourceClassName("org.h2.Driver")
                                .xaDatasourceClassName("org.h2.jdbcx.JdbcDataSource")
                                .module("com.h2database.h2"))
                        .datasource(new Datasource("MyDS")
                                .driver("h2")
                                .connectionURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                                .authentication("sa", "sa"))
        );

        // Prevent JPA Fraction from installing it's default datasource fraction
        container.fraction(new JPAFraction()
                        .inhibitDefaultDatasource()
                        .defaultDatasourceName("MyDS")
        );

        //container.fraction(LoggingFraction.createTraceLoggingFraction());

        container.start();

        JAXRSDeployment deployment = new JAXRSDeployment(container);
        deployment.getArchive().addClass(Employee.class);
        deployment.getArchive().addClass(Resources.class);
        deployment.getArchive().addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        deployment.getArchive().addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");

        deployment.getArchive().addAsWebInfResource(new StringAsset("<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"\n" +
                "        http://xmlns.jcp.org/xml/ns/javaee\n" +
                "        http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\" bean-discovery-mode=\"all\">\n" +
                "</beans>"), "beans.xml");
        deployment.addResource(EmployeeResource.class);

        container.deploy(deployment);
    }
}

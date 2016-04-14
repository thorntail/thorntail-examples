package org.wildfly.swarm.examples.ds.war;

import java.util.Arrays;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Running " + Main.class.getCanonicalName() + ".main");

        Container container = new Container();

        String useDB = System.getProperty("swarm.use.db", "h2");

        // Configure the Datasources subsystem with a driver
        // and a datasource
        switch (useDB.toLowerCase()) {
            case "h2":
                container.fraction(datasourceWithH2()); break;
            case "postgresql" :
                container.fraction(datasourceWithPostgresql()); break;
            case "mysql" :
                container.fraction(datasourceWithMysql()); break;
            default:
                container.fraction(datasourceWithH2());
        }

        // Start the container and deploy the default war
        container.start().deploy();
    }

    private static DatasourcesFraction datasourceWithH2() {
        return new DatasourcesFraction()
                .jdbcDriver("h2", (d) -> {
                    d.driverClassName("org.h2.Driver");
                    d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                    d.driverModuleName("com.h2database.h2");
                })
                .dataSource("ExampleDS", (ds) -> {
                    ds.driverName("h2");
                    ds.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
                    ds.userName("sa");
                    ds.password("sa");
                });
    }

    private static DatasourcesFraction datasourceWithPostgresql() {
        return new DatasourcesFraction()
                .jdbcDriver("org.postgresql", (d) -> {
                    d.driverClassName("org.postgresql.Driver");
                    d.xaDatasourceClass("org.postgresql.xa.PGXADataSource");
                    d.driverModuleName("org.postgresql");
                })
                .dataSource("ExampleDS", (ds) -> {
                    ds.driverName("org.postgresql");
                    ds.connectionUrl("jdbc:postgresql://localhost:5432/postgres");
                    ds.userName("postgres");
                    ds.password("postgres");
                });
    }

    private static DatasourcesFraction datasourceWithMysql() {
        return new DatasourcesFraction()
                .jdbcDriver("com.mysql", (d) -> {
                    d.driverClassName("com.mysql.jdbc.Driver");
                    d.xaDatasourceClass("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
                    d.driverModuleName("com.mysql");
                })
                .dataSource("ExampleDS", (ds) -> {
                    ds.driverName("com.mysql");
                    ds.connectionUrl("jdbc:mysql://localhost:3306/mysql");
                    ds.userName("root");
                    ds.password("root");
                });
    }

}

package org.wildfly.swarm.examples.camel.jpa;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/example-camel-jpa")
public class MyApplication extends Application {

    public MyApplication() throws IOException {
    	InputStream input = getClass().getResourceAsStream("/customers/customer-john.xml");
    	Path destination = new File(System.getProperty("jboss.server.data.dir")).toPath().resolve("customers");
		Files.copy(input, destination.resolve("customer.xml"));
    }
}

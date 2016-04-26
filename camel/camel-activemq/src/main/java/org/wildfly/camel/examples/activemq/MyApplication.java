package org.wildfly.camel.examples.activemq;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/example-camel-activemq")
public class MyApplication extends Application {

    public MyApplication() throws IOException {
		InputStream input = getClass().getResourceAsStream("/order.xml");
		Path destination = new File(System.getProperty("jboss.server.data.dir")).toPath().resolve("orders");
		Files.copy(input, destination.resolve("order.xml"), StandardCopyOption.REPLACE_EXISTING);
    }
}

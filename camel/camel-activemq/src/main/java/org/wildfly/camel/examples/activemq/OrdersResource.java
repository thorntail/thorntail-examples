package org.wildfly.camel.examples.activemq;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/orders/{country}")
public class OrdersResource {

    @GET
    public String get(@PathParam("country") String country) throws IOException {
		File datadir = new File(System.getProperty("jboss.server.data.dir"));
		File countrydir = datadir.toPath().resolve("orders/processed/" + country).toFile();
		return country + ": " + countrydir.list().length;
	}
}

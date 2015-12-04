package org.wildfly.swarm.it.jaxrs;

import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * An example client component that invokes on a service.
 * @author Heiko Braun
 * @since 03/12/15
 */
public class ProviderClient {

    private final String url;

    public ProviderClient(String url) {
        this.url = url;
    }

    public String hello(String name) throws IOException {
        String response = Request.Post(url + "/")
                .addHeader("client-name", name)
                .execute()
                .returnContent()
                .asString();
        return response;
    }
}

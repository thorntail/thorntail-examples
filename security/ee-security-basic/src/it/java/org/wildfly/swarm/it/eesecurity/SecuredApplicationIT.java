package org.wildfly.swarm.it.eesecurity;

import static org.fest.assertions.Assertions.assertThat;

import java.nio.charset.Charset;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wildfly.swarm.it.AbstractIntegrationTest;

/**
 * An integration test verifying our servlet and rest endpoint responses with authenticated and non-authenticated
 * clients.
 *
 * @author Tomas Hofman
 */
public class SecuredApplicationIT extends AbstractIntegrationTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String USER = "user";
    private static final String PASSWORD = "user";

    private Client client;

    @Before
    public void setup() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testNotAuthenticated() throws Exception {
        WebTarget target = client.target(BASE_URL);

        // jaxrs endpoint
        Response response = target.path("/whoami").request().get();
        assertThat(response.readEntity(Map.class).get("logged in")).isEqualTo(false);

        // servlet
        response = target.path("/servlet").request().get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void testAuthenticated() throws Exception {
        WebTarget target = client.target(BASE_URL);
        String authHeader = getAuthHeader();

        // jaxrs endpoint
        Response response = target.path("/whoami").request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .get();
        assertThat(response.readEntity(Map.class).get("logged in")).isEqualTo(true);

        // servlet
        response = target.path("/servlet").request()
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    private String getAuthHeader() {
        String auth = "user:user";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
        return "Basic " + new String(encodedAuth);
    }

}

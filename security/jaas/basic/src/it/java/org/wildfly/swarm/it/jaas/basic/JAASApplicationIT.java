package org.wildfly.swarm.it.jaas.basic;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wildfly.swarm.examples.jaas.basic.Employee;
import org.wildfly.swarm.it.AbstractIntegrationTest;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Yoshimasa Tanabe
 */
public class JAASApplicationIT extends AbstractIntegrationTest {

    Client client;
    WebTarget target;

    @Before
    public void setup() throws Exception {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080");
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testNotAuthed() throws Exception {
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void testAuthenticated() throws Exception {
        Response response = target.request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic UGVubnk6cGFzc3dvcmQ=")
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        List<Employee> employees = response.readEntity(new GenericType<List<Employee>>(){});

        assertThat(employees.size()).isEqualTo(8);
    }

}

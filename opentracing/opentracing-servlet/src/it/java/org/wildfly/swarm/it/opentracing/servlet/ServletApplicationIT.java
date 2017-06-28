package org.wildfly.swarm.it.opentracing.servlet;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.swarm.it.AbstractIntegrationTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Bob McWhirter
 * @author Juraci Paixão Kröhling
 */
@RunWith(Arquillian.class)
public class ServletApplicationIT extends AbstractIntegrationTest {

    @Test
    public void testIt() throws Exception {
        hitEndpoint();
    }

    private void hitEndpoint() throws IOException {
        InputStream response = new URL("http://localhost:8080/_opentracing/hello").openStream();
        String contents;
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(response))) {
            contents = buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}

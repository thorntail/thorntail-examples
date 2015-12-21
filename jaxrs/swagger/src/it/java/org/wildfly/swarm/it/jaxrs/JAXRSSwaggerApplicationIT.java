package org.wildfly.swarm.it.jaxrs;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.swarm.it.AbstractIntegrationTest;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Bob McWhirter
 */
@RunWith(Arquillian.class)
public class JAXRSSwaggerApplicationIT extends AbstractIntegrationTest {

    @Drone
    WebDriver browser;

    @Test
    public void testIt() {
        browser.navigate().to("http://localhost:8080/time/now");
        assertThat(browser.getPageSource()).contains("The time is");
    }

    @Test
    public void testSwagger() {
        browser.navigate().to("http://localhost:8080/swagger.json");
        assertThat(browser.getPageSource()).contains("Get the current time");
    }
}

package org.wildfly.swarm.it.camel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.wildfly.swarm.it.AbstractIntegrationTest;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Bob McWhirter
 */
@RunWith(Arquillian.class)
public class CamelApplicationIT extends AbstractIntegrationTest {

    private static Pattern REGEXP = Pattern.compile("Hello bob, how are you\\? You are from: [A-Z]+");

    @Drone
    WebDriver browser;

    @Test
    public void testIt() {
        browser.navigate().to("http://localhost:8080/camel/hello");
        assertThat(browser.getPageSource()).contains("Add a name parameter to uri, eg ?name=foo");

        browser.navigate().to("http://localhost:8080/camel/hello?name=bob");

        WebElement pre = browser.findElement(By.cssSelector("pre"));

        Matcher matcher = REGEXP.matcher(pre.getText());
        assertThat( matcher.matches() ).isTrue();
    }
}

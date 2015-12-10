package org.wildfly.swarm.it.ribbon;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.jboss.arquillian.phantom.resolver.ResolvingPhantomJSDriverService;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.wildfly.swarm.it.AbstractIntegrationTest;

import static org.fest.assertions.Assertions.assertThat;
/**
 * @author Bob McWhirter
 */
public class RibbonIT extends AbstractIntegrationTest {

    PhantomJSDriver browser;

    @Before
    public void setup() throws IOException {
        DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();

        capabilities.setCapability("phantomjs.prefer.resolved", Boolean.FALSE);

        this.browser = new PhantomJSDriver(
                ResolvingPhantomJSDriverService.createDefaultService(capabilities),
                capabilities);
    }

    @Test
    public void testIt() {
        browser.setLogLevel(Level.ALL);
        browser.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        browser.navigate().to("http://localhost:8080/");

        List<WebElement> timeServices = browser.findElementsByCssSelector(".time-service .service-address");
        assertThat( timeServices ).hasSize(2);
    }
}

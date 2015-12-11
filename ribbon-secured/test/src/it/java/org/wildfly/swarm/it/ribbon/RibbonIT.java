package org.wildfly.swarm.it.ribbon;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.phantom.resolver.ResolvingPhantomJSDriverService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.wildfly.swarm.it.AbstractIntegrationTest;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
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

    static Pattern TIME_REGEX = Pattern.compile( "\\d\\d?:\\d\\d?:\\d\\d?.\\d\\d?\\d? on \\d\\d\\d\\d-\\d\\d?-\\d\\d?" );

    @Test
    @Ignore
    public void testNotAuthed() throws Exception {
        browser.setLogLevel(Level.ALL);
        browser.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        browser.navigate().to("http://localhost:8080/");

        List<WebElement> timeServices = browser.findElementsByCssSelector(".time-service .service-address");
        assertThat( timeServices ).hasSize(2);

        List<WebElement> eventServices = browser.findElementsByCssSelector(".event-service .service-address");
        assertThat( eventServices ).hasSize(1);

        WebElement getTimeButton = browser.findElementByCssSelector(".time-service .get-btn");
        assertThat( getTimeButton ).isNotNull();

        getTimeButton.click();

        Thread.sleep( 2000 );

        List<WebElement> timestamp = browser.findElementsByCssSelector(".time-service .timestamp");
        assertThat( timestamp ).isEmpty();

        WebElement getEventButton = browser.findElementByCssSelector(".time-service .get-btn");
        assertThat( getEventButton ).isNotNull();

        getEventButton.click();

        Thread.sleep( 2000 );

        List<WebElement> event = browser.findElementsByCssSelector(".event-service .event");
        assertThat( event ).isEmpty();
    }

    @Test
    public void testAuthenticated() throws Exception {
        browser.setLogLevel(Level.ALL);
        browser.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        browser.navigate().to("http://localhost:8080/");

        WebElement login = browser.findElementById("login");
        assertThat( login ).isNotNull();
        login.click();

        browser.findElementById( "username" ).sendKeys( "bob" );
        browser.findElementById( "password" ).sendKeys( "tall" );
        browser.findElementById( "kc-login" ).click();

        Thread.sleep( 2000 );

        WebElement logout = browser.findElementById("logout");
        assertThat( logout ).isNotNull();

        List<WebElement> timeServices = browser.findElementsByCssSelector(".time-service .service-address");
        assertThat( timeServices ).hasSize(2);

        List<WebElement> eventServices = browser.findElementsByCssSelector(".event-service .service-address");
        assertThat( eventServices ).hasSize(1);

        WebElement getTimeButton = browser.findElementByCssSelector(".time-service .get-btn");
        assertThat( getTimeButton ).isNotNull();

        getTimeButton.click();

        Thread.sleep( 2000 );

        WebElement timestamp = browser.findElementByCssSelector(".time-service .timestamp");
        assertThat( timestamp ).isNotNull();

        Matcher matcher = TIME_REGEX.matcher(timestamp.getText());
        assertThat( matcher.matches() ).isTrue();

        WebElement getEventButton = browser.findElementByCssSelector(".event-service .get-btn");
        assertThat( getEventButton ).isNotNull();

        getEventButton.click();

        Thread.sleep( 2000 );

        WebElement event = browser.findElementByCssSelector(".event-service .event");
        assertThat( event ).isNotNull();

        WebElement eventTimestamp = event.findElement(By.cssSelector(".timestamp"));
        assertThat( eventTimestamp ).isNotNull();

        matcher = TIME_REGEX.matcher(eventTimestamp.getText());
        assertThat( matcher.matches() ).isTrue();
    }
}

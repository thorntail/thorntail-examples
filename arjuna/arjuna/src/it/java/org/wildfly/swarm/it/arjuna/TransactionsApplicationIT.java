package org.wildfly.swarm.it.arjuna;

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
public class TransactionsApplicationIT extends AbstractIntegrationTest {

    @Drone
    WebDriver browser;

    @Test
    public void testActive() {
        browser.navigate().to("http://localhost:8080/");
        assertThat(browser.getPageSource()).contains("Active");
    }

    @Test
    public void testAtomicAction() {
        browser.navigate().to("http://localhost:8080/atomicaction");
        assertThat(browser.getPageSource()).contains("Begin BasicAction");
	assertThat(browser.getPageSource()).contains("Committed BasicAction");
    }

    @Test
    public void testBeginCommit() {
        browser.navigate().to("http://localhost:8080/begincommit");
        assertThat(browser.getPageSource()).contains("Transaction begun ok and committed ok" );
    }

    @Test
    public void testBeginRollback() {
        browser.navigate().to("http://localhost:8080/beginrollback");
        assertThat(browser.getPageSource()).contains("Transaction begun ok and rolled back ok" );
    }

    @Test
    public void testNestedChildCommit() {
        browser.navigate().to("http://localhost:8080/nestedChildCommit");
        assertThat(browser.getPageSource()).contains("Child and parent committed ok!");
    }

    @Test
    public void testNestedChildAbort() {
        browser.navigate().to("http://localhost:8080/nestedChildAbort");
        assertThat(browser.getPageSource()).contains("Child aborted and parent still committed ok!");
    }    
}

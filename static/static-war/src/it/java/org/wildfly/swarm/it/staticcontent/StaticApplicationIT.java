package org.wildfly.swarm.it.staticcontent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
public class StaticApplicationIT extends AbstractIntegrationTest {

    @Drone
    WebDriver browser;

    @Test
    public void testIt() throws Exception {
        browser.navigate().to("http://localhost:8080/");
        assertThat(browser.getPageSource()).contains("Howdy!");

        // This test only works when run from Maven anyway, so go ahead and
        // assume the system property we set in Maven is always there
        Path testWebDir = Paths.get(System.getProperty("test.web.dir"));
        Path indexPath = testWebDir.resolve("index.html");
        Path fooPath = testWebDir.resolve("foo.html");

        try {
            Files.copy(indexPath, fooPath, StandardCopyOption.REPLACE_EXISTING);
            browser.navigate().to("http://localhost:8080/foo.html");
            assertThat(browser.getPageSource()).contains("Howdy!");

            Files.write(fooPath, "Howdy from foo!".getBytes());
            browser.navigate().refresh();
            assertThat(browser.getPageSource()).contains("Howdy from foo!");
        } finally {
             Files.delete(fooPath);
        }
    }
}

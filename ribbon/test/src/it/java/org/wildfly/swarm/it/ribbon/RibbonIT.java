package org.wildfly.swarm.it.ribbon;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.wildfly.swarm.it.AbstractIntegrationTest;

/**
 * @author Bob McWhirter
 */
@RunWith(Arquillian.class)
public class RibbonIT extends AbstractIntegrationTest {

    @Drone
    PhantomJSDriver browser;

    @Test
    public void testIt() {
        browser.navigate().to("http://localhost:8080/");
    }
}

package org.wildfly.swarm.examples.ds.deployment;

import javax.naming.Context;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.swarm.examples.rar.deployment.FileIOBean;
import org.wildfly.swarm.it.AbstractIntegrationTest;
import org.wildfly.swarm.it.Log;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Bob McWhirter
 */
@RunWith(Arquillian.class)
public class ResourceAdapterIT extends AbstractIntegrationTest {

    @Test
    public void testIt() throws Exception {
        Log log = getStdOutLog();
        assertThatLog( log ).hasLineContaining( "WFLYJCA0002: Bound JCA ConnectionFactory [java:global/XADiskCF]" );
        assertThatLog( log ).hasLineContaining( "WFLYSRV0010: Deployed \"xadisk.rar\"" );
        assertThatLog( log ).hasLineContaining( "java:module/FileIOBean");        
    }

}

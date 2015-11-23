package org.wildfly.swarm.examples.msc;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.it.AbstractIntegrationTest;
import org.wildfly.swarm.it.Log;

/**
 * @author Bob McWhirter
 */
@RunWith(Arquillian.class)
public class MSCApplicationIT extends AbstractIntegrationTest {
    @Test
    public void testIt() throws Exception {
        Thread.sleep( 3000 );
        Log log = getStdErrLog();

        assertThatLog( log ).hasLineContaining( "Hi #1" );
        assertThatLog( log ).hasLineContaining( "Hi #2" );
        assertThatLog( log ).hasLineContaining( "Howdy #1" );
        assertThatLog( log ).hasLineContaining( "Howdy #2" );
    }
}

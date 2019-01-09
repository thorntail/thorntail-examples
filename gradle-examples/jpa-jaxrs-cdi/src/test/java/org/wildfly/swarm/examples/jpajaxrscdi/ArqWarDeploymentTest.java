package org.wildfly.swarm.examples.jpajaxrscdi;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * Example test showcasing the use of Arquillian within a Gradle project.
 */
@RunWith(Arquillian.class)
public class ArqWarDeploymentTest {

    @ArquillianResource
    private URL url;

    @Deployment
    public static Archive createDeployment() throws Exception {
        WARArchive archive = ShrinkWrap.create(WARArchive.class, "SampleTest.war");
        archive.addPackage(MyApplication.class.getPackage());
        archive.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        archive.addAsResource("META-INF/load.sql", "META-INF/load.sql");
        archive.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml");

        // Include the test case related dependencies (non-fractions) explicitly.
        // Do not use the addAllDependencies() as that will pull in dependencies that won't be satisfied in a test case.
        // If you ended up on https://issues.jboss.org/browse/THORN-1072 via a google search, then make sure that you remove
        // the addAllDependencies() invocation.
        archive.addDependency("commons-io:commons-io:2.4");
        return archive;
    }

    @Test
    @RunAsClient
    public void testAPIInvocation() throws IOException {
        String content = IOUtils.toString(url, Charset.forName("UTF-8").toString());

        assertTrue(content.contains("{\"id\":1,\"name\":\"Penny\"}"));
        assertTrue(content.contains("{\"id\":2,\"name\":\"Sheldon\"}"));
        assertTrue(content.contains("{\"id\":3,\"name\":\"Amy\"}"));
        assertTrue(content.contains("{\"id\":4,\"name\":\"Leonard\"}"));
        assertTrue(content.contains("{\"id\":5,\"name\":\"Bernadette\"}"));
        assertTrue(content.contains("{\"id\":6,\"name\":\"Raj\"}"));
        assertTrue(content.contains("{\"id\":7,\"name\":\"Howard\"}"));
        assertTrue(content.contains("{\"id\":8,\"name\":\"Priya\"}"));
    }
}

package org.wildfly.swarm.it;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Bob McWhirter
 */
public class AbstractIntegrationTest {

    protected Log getStdOutLog() throws Exception {
        return getLog("target/stdout.log");
    }

    protected Log getStdErrLog() throws Exception {
        return getLog("target/stderr.log");
    }

    protected Log getLog(String path) throws IOException {
        return new Log(Files.readAllLines(new File(path).toPath()));
    }

    public LogAssert assertThatLog(Log log) {
        return new LogAssert(log);
    }
}

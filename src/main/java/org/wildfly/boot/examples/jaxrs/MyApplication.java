package org.wildfly.boot.examples.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author Bob McWhirter
 */
@ApplicationPath("/")
public class MyApplication extends Application {

    public MyApplication() {
    }
}

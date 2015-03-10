package org.projectodd.myapp;

import org.wildfly.selfcontained.Container;
import org.wildfly.selfcontained.IoSubsystem;
import org.wildfly.selfcontained.LoggingSubsystem;
import org.wildfly.selfcontained.UndertowSubsystem;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) {
        Container container = new Container();

        container
                .subsystem(new LoggingSubsystem()
                                .formatter("PATTERN", "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n")
                                .consoleHandler("TRACE", "PATTERN")
                                .rootLogger("CONSOLE", "TRACE")
                )
                .subsystem(new IoSubsystem())
                .subsystem(new UndertowSubsystem())
                .iface("public", "${jboss.bind.address:127.0.0.1}")
                .socketBindingGroup("default-sockets", "public", "0")
                .socketBinding("http", 8080).end()
                .start();
    }
}

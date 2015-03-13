package org.projectodd.myapp;


import org.wildfly.boot.container.Container;
import org.wildfly.boot.container.SocketBindingGroup;
import org.wildfly.boot.core.LoggingSubsystem;
import org.wildfly.boot.core.RequestControllerSubsystem;
import org.wildfly.boot.web.EeSubsystem;
import org.wildfly.boot.web.IoSubsystem;
import org.wildfly.boot.web.NamingSubsystem;
import org.wildfly.boot.web.SecuritySubsystem;
import org.wildfly.boot.web.UndertowSubsystem;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) {
        Container container = new Container();

        container
                .subsystem(new NamingSubsystem())
                .subsystem(new EeSubsystem())
                .subsystem(new SecuritySubsystem())
                .subsystem(new RequestControllerSubsystem())
                .subsystem(new LoggingSubsystem()
                                .formatter("PATTERN", "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n")
                                .consoleHandler("INFO", "PATTERN")
                                .rootLogger("CONSOLE", "INFO")
                )
                .subsystem(new IoSubsystem())
                .subsystem(new UndertowSubsystem())
                .iface("public", "${jboss.bind.address:127.0.0.1}")
                .socketBindingGroup(
                        new SocketBindingGroup("default-sockets", "public", "0")
                                .socketBinding("http", 8080)
                )
                .start();
    }
}

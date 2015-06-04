package org.wildfly.swarm.examples.msc;

import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

/**
 * @author Bob McWhirter
 */
public class MyService implements Service<Void> {

    private final String message;

    private Thread thread;

    private boolean shouldRun;

    public MyService(String message) {
        this.message = message;
    }

    public void start(final StartContext startContext) throws StartException {
        startContext.asynchronous();
        this.shouldRun = true;
        this.thread = new Thread(new Runnable() {
            public void run() {
                startContext.complete();

                while (shouldRun) {
                    System.err.println(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });

        this.thread.start();
    }

    public void stop(StopContext stopContext) {
        this.shouldRun = false;
    }

    public Void getValue() throws IllegalStateException, IllegalArgumentException {
        return null;
    }
}


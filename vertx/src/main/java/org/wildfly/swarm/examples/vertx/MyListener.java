package org.wildfly.swarm.examples.vertx;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.resource.ResourceException;

import io.vertx.core.eventbus.Message;
import io.vertx.resourceadapter.VertxConnection;
import io.vertx.resourceadapter.VertxConnectionFactory;
import io.vertx.resourceadapter.inflow.VertxListener;
import org.jboss.ejb3.annotation.ResourceAdapter;

/**
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@MessageDriven(name = "VertxMonitor",
        messageListenerInterface = VertxListener.class,
        activationConfig = {
                @ActivationConfigProperty(propertyName = "address", propertyValue = "tacos")
        })
@ResourceAdapter("vertx-ra")
public class MyListener implements VertxListener{

    @Override
    public <String> void onMessage(Message<String> message) {
        System.out.println("MESSAGE: "+message.body());
    }
}

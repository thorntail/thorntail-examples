package org.wildfly.swarm.examples.multi.events;

import com.netflix.ribbon.ClientOptions;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.http.HttpResourceGroup;
import io.netty.buffer.ByteBuf;
import rx.Observable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.nio.charset.Charset;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class EventsResource {

    private final TimeService time;

    public EventsResource() {
        ClientOptions options = ClientOptions.create();
        options.withConfigurationBasedServerList("localhost:8181");
        HttpResourceGroup resourceGroup = Ribbon.createHttpResourceGroup( "time", options );
        this.time = Ribbon.from( TimeService.class, resourceGroup );
    }

    @GET
    @Produces("application/json")
    public void get(@Suspended final AsyncResponse asyncResponse) {
        Observable<ByteBuf> obs = this.time.currentTime().observe();

        obs.subscribe(
                (result) -> {
                    asyncResponse.resume(result.toString(Charset.defaultCharset() ) );
                },
                (err) -> {
                    asyncResponse.resume(err);
                });
    }
}

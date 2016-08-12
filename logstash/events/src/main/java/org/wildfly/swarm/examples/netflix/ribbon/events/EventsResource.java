package org.wildfly.swarm.examples.netflix.ribbon.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import rx.Observable;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class EventsResource {

    private final TimeService time;

    public EventsResource() {
        //this.time = Ribbon.from( TimeService.class );
        this.time = TimeService.INSTANCE;
    }

    @GET
    @Produces("application/json")
    public void get(@Suspended final AsyncResponse asyncResponse) {
        Observable<ByteBuf> obs = this.time.currentTime().observe();

        obs.subscribe(
                (result) -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        ObjectReader reader = mapper.reader();
                        JsonFactory factory = new JsonFactory();
                        JsonParser parser = factory.createParser(new ByteBufInputStream(result));
                        Map map = reader.readValue(parser, Map.class);
                        int hour = (int) map.get( "h" );
                        List<String> events = new ArrayList<>();
                        for ( int i = 1 ; i <= 10 ; ++i ) {
                            events.add( "Event #" + i + " at " + hour + ":00" );
                        }
                        asyncResponse.resume(events);
                    } catch (IOException e) {
                        asyncResponse.resume(e);
                    }
                },
                (err) -> {
                    asyncResponse.resume(err);
                });
    }
}

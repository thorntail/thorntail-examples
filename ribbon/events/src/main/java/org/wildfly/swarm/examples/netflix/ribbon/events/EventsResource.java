package org.wildfly.swarm.examples.netflix.ribbon.events;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.netflix.ribbon.Ribbon;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import rx.Observable;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Produces(MediaType.APPLICATION_JSON)
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
                        int minute = (int) map.get( "m" );
                        int millis = (int) map.get( "ms" );
                        String tz = (String) map.get( "tz" );
                        List<String> events = new ArrayList<>();
                        for ( int i = 1 ; i <= 10 ; ++i ) {
                            StringBuffer buffer = new StringBuffer("Event #")
                                    .append(i)
                                    .append(" at ")
                                    .append(hour)
                                    .append(":")
                                    .append(minute)
                                    .append(".")
                                    .append(millis)
                                    .append(" ")
                                    .append(tz);

                            events.add( buffer.toString() );
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

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, undefined")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }
}

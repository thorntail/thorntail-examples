package org.wildfly.swarm.examples.netflix.ribbon.time;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class TimeResource {

    public TimeResource() {
        System.out.println( "TimeResource ctor" );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        System.out.println( "I was asked for the time" );
        Map<String,Object> t = new HashMap<>();
        DateTime d = new DateTime();


        t.put( "Y", d.getYear() );
        t.put( "M", d.getMonthOfYear() );
        t.put( "D", d.getDayOfMonth() );
        t.put( "h", d.getHourOfDay() );
        t.put( "m", d.getMinuteOfHour() );
        t.put( "s", d.getSecondOfMinute() );
        t.put( "ms", d.getMillisOfSecond() );
        t.put( "tz", d.getZone().getID() );

        return Response.ok(t, MediaType.APPLICATION_JSON_TYPE)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, undefined")
                .header("Access-Control-Max-Age", "1209600")
                .entity(t)
                .build();
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        System.out.println( "Time pre-flight" );
        return Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, undefined")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }
}

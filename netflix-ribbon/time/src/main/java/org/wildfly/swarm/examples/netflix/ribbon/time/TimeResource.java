package org.wildfly.swarm.examples.netflix.ribbon.time;

import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bob McWhirter
 */
@Path("/")
public class TimeResource {

    @GET
    @Produces("application/json")
    public Map<String,Object> get() {
        Map<String,Object> t = new HashMap<>();
        DateTime d = new DateTime();


        t.put( "Y", d.getYear() );
        t.put( "M", d.getMonthOfYear() );
        t.put( "D", d.getDayOfMonth() );
        t.put( "h", d.getHourOfDay() );
        t.put( "m", d.getMinuteOfDay() );
        t.put( "s", d.getSecondOfMinute() );
        t.put( "ms", d.getMillisOfDay() );
        t.put( "tz", d.getZone().getID() );

        return t;
    }
}

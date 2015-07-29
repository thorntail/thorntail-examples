package org.wildfly.swarm.examples.netflix.ribbon.events;

import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.*;
import io.netty.buffer.ByteBuf;
import org.wildfly.swarm.netflix.ribbon.secured.SecuredRibbon;

/**
 * @author Bob McWhirter
 */
@ResourceGroup( name="time" )
public interface TimeService {

    TimeService INSTANCE = SecuredRibbon.from( TimeService.class );

    @TemplateName("currentTime")
    @Http(
            method = Http.HttpMethod.GET,
            uri = "/"
    )
    @Hystrix(
            fallbackHandler = TimeFallbackHandler.class
    )
    RibbonRequest<ByteBuf> currentTime();

}

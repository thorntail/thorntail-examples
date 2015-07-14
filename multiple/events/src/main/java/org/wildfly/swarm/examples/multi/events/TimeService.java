package org.wildfly.swarm.examples.multi.events;

import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.ribbon.proxy.annotation.ResourceGroup;
import com.netflix.ribbon.proxy.annotation.TemplateName;
import io.netty.buffer.ByteBuf;

/**
 * @author Bob McWhirter
 */
@ResourceGroup( name="time" )
public interface TimeService {

    @TemplateName("currentTime")
    @Http(
            method = Http.HttpMethod.GET,
            uriTemplate = "/"
    )
    RibbonRequest<ByteBuf> currentTime();
}

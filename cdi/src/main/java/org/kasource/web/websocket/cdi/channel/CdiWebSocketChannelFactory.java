package org.kasource.web.websocket.cdi.channel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.server.ServerChannel;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.channel.server.ServerChannelFactoryImpl;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.listener.WebSocketEventListener;

@ApplicationScoped
public class CdiWebSocketChannelFactory extends ServerChannelFactoryImpl {

    
    
   
    /**
     * Invoked when a initialized ServletContext has been published.
     * 
     * @param servletContext ServletContext to use.
     **/
    public void onServletContextInitialized(@Observes ServletContext servletContext) {
        try {
            this.initialize(servletContext);
        } catch (Exception e) {
           throw new WebSocketConfigException("Could not initialize " + CdiWebSocketChannelFactory.class.getName());
        }

    }

    
}

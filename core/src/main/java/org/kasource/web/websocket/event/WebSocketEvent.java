package org.kasource.web.websocket.event;

import java.util.EventObject;

import org.kasource.web.websocket.channel.server.ServerChannel;

/**
 * Base event for all web socket events.
 * 
 * @author rikardwi
 **/
public abstract class WebSocketEvent extends EventObject {

    private static final long serialVersionUID = 1L;
   
    
    /**
     * Constructor.
     *  
     * @param websocket The web socket that caused the to be emitted.
     **/
    public WebSocketEvent(ServerChannel websocket) {
        super(websocket);
    }
    
    /**
     * Returns the web socket that is the source for the event.
     *
     * @return web socket that's the source
     **/
    @Override
    public ServerChannel getSource() {
        return (ServerChannel) super.getSource();
    }

   
}

package org.kasource.web.websocket.listener;

import java.util.EventListener;

import org.kasource.web.websocket.event.ClientConnectionEvent;

/**
 * Listener interface for WebSocketClientConnectionEventEvent.
 * 
 * @author rikardwi
 **/
public interface ClientConnectionListener extends EventListener {
    
    /**
     * Called when a client has connected.
     * 
     * @param event Connection event
     **/
    public void onClientConnection(ClientConnectionEvent event);
}

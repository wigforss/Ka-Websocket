package org.kasource.web.websocket.listener;

import java.util.EventListener;

import org.kasource.web.websocket.event.ClientDisconnectedEvent;

/**
 * Listener interface for WebSocketClientDisconnectedEvent.
 * 
 * @author rikardwi
 **/
public interface ClientDisconnectedListener extends EventListener {
    
    /**
     * Called when a client has been disconnected.
     * 
     * @param event Disconnection event
     */
    public void onClientDisconnected(ClientDisconnectedEvent event);
}

package org.kasource.web.websocket.listener.impl;

import org.kasource.web.websocket.event.ClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.listener.ClientConnectionListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;

public class ClientConnectedHandler implements WebSocketEventListener {
   private ClientConnectionListener listener;
    
    public ClientConnectedHandler(ClientConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if (event instanceof ClientConnectionEvent){
            listener.onClientConnection((ClientConnectionEvent) event);
        }
        
    }
    
}

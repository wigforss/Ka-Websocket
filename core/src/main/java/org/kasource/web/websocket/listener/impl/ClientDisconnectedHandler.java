package org.kasource.web.websocket.listener.impl;

import org.kasource.web.websocket.event.ClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.listener.ClientDisconnectedListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;

public class ClientDisconnectedHandler implements WebSocketEventListener {
   private ClientDisconnectedListener listener;
    
    public ClientDisconnectedHandler(ClientDisconnectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if (event instanceof ClientDisconnectedEvent){
            listener.onClientDisconnected((ClientDisconnectedEvent) event);
        }
        
    }
    
}

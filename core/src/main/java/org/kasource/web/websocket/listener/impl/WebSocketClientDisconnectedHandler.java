package org.kasource.web.websocket.listener.impl;

import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.listener.WebSocketClientDisconnectedListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;

public class WebSocketClientDisconnectedHandler implements WebSocketEventListener {
   private WebSocketClientDisconnectedListener listener;
    
    public WebSocketClientDisconnectedHandler(WebSocketClientDisconnectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if (event instanceof WebSocketClientDisconnectedEvent){
            listener.onClientDisconnected((WebSocketClientDisconnectedEvent) event);
        }
        
    }
    
}

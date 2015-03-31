package org.kasource.web.websocket.listener.impl;

import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.listener.WebSocketClientConnectionListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;

public class WebSocketClientConnectedHandler implements WebSocketEventListener {
   private WebSocketClientConnectionListener listener;
    
    public WebSocketClientConnectedHandler(WebSocketClientConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if (event instanceof WebSocketClientConnectionEvent){
            listener.onClientConnection((WebSocketClientConnectionEvent) event);
        }
        
    }
    
}

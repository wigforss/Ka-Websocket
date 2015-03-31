package org.kasource.web.websocket.listener.impl;

import org.kasource.web.websocket.event.WebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.listener.WebSocketBinaryMessageListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;

public class WebSocketBinaryMessageHandler implements WebSocketEventListener{
   private WebSocketBinaryMessageListener listener;
    
    public WebSocketBinaryMessageHandler(WebSocketBinaryMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if (event instanceof WebSocketBinaryMessageEvent){
            listener.onBinaryMessage((WebSocketBinaryMessageEvent) event);
        }
        
    }
    
}

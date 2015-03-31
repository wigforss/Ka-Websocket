package org.kasource.web.websocket.listener.impl;

import org.kasource.web.websocket.event.BinaryMessageEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.listener.BinaryMessageListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;

public class BinaryMessageHandler implements WebSocketEventListener{
   private BinaryMessageListener listener;
    
    public BinaryMessageHandler(BinaryMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if (event instanceof BinaryMessageEvent){
            listener.onBinaryMessage((BinaryMessageEvent) event);
        }
        
    }
    
}

package org.kasource.web.websocket.listener.impl;

import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.TextMessageEvent;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.listener.TextMessageListener;

public class TextMessageHandler implements WebSocketEventListener {
   private TextMessageListener listener;
    
    public TextMessageHandler(TextMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if (event instanceof TextMessageEvent){
            listener.onMessage((TextMessageEvent)event);
        }
        
    }
    
}

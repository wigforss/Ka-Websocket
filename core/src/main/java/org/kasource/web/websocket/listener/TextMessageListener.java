package org.kasource.web.websocket.listener;

import java.util.EventListener;

import org.kasource.web.websocket.event.TextMessageEvent;

/**
 * Listener interface for WebSocketBinaryMessageEvennt.
 * 
 * @author rikardwi
 */
public interface TextMessageListener extends EventListener {
   
    /**
     * Called when a new text message have been received.
     * 
     * @param event Message event
     **/
    public void onMessage(TextMessageEvent event);
}

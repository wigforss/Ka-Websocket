package org.kasource.web.websocket.event.extension.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.extension.SendObjectAsWebSocketBinaryMessageEvent;

/**
 * Listener Interface for SendWebSocketBinaryMessageEvent.
 * 
 * @author rikardwi
 **/
public interface SendWebSocketBinaryProtocolMessageListener extends EventListener {

    /**
     * Called when a binary message should be sent to a specific client.
     * 
     * @param event message send event.
     */
    public void onSendWebSocketBinaryMessage(SendObjectAsWebSocketBinaryMessageEvent event);
    
}
package org.kasource.web.websocket.listener;

import java.util.EventListener;

import org.kasource.web.websocket.event.BinaryMessageEvent;

/**
 * Listener interface for WebSocketBinaryMessageEvennt.
 * 
 * @author rikardwi
 */
public interface BinaryMessageListener extends EventListener {

    /**
     * Called when a new binary message have been received.
     * 
     * @param event Message event
     **/
    public void onBinaryMessage(BinaryMessageEvent event);
}

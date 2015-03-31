package org.kasource.web.websocket.channel;

import java.io.IOException;

import org.kasource.web.websocket.client.RecipientType;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.protocol.ConversionException;
import org.kasource.web.websocket.protocol.ProtocolHandler;



/**
 * Web Socket Channel.
 * 
 * Exposes methods that can be invoked on an underlying Web Socket Servlet.
 * 
 * @author rikardwi
 **/
public interface WebSocketChannel extends WebsocketMessageSender {
    
    

    
    /**
     * Adds an event listener.
     * 
     * @param listener Event listener to add.
     **/
    public void addListener(WebSocketEventListener listener);
    
    /**
     * Returns the URL of the web socket.
     * 
     * @return URL of the web socket.
     **/
    public String getUrl();
}

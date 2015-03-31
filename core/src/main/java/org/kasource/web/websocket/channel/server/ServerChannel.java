package org.kasource.web.websocket.channel.server;

import org.kasource.web.websocket.channel.MessageSender;
import org.kasource.web.websocket.listener.WebSocketEventListener;



/**
 * Web Socket Server Channel.
 * 
 * Server side code can register listeners to listen on events for
 * a certain URL. When a client channel becomes available for such an
 * URL the listeners will start getting notifications.
 * 
 * Messages can be sent to either all clients on the client channel (broadcast), all 
 * connections for a certain username (toUsername) or to a specific client connection (toClient).
 * 
 * Sending messages when a client channel has not been established (no clients connected yet) will not
 * have any effect.
 * 
 * @author rikardwi
 **/
public interface ServerChannel extends MessageSender {
    
    

    
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

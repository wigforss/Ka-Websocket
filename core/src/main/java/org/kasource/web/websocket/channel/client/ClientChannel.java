package org.kasource.web.websocket.channel.client;


import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.channel.ClientChannelListener;
import org.kasource.web.websocket.channel.MessageSender;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.security.AuthenticationProvider;


/**
 * Web Socket Client Channel
 * 
 * Client Channels are established lazily when clients connect via 
 * a common URL pattern. These channels are published as attributes in
 * the ServletContext.
 * 
 * Server Channels can be bound to Clients Channels once the first client connects.
 * 
 * @author rikardwi
 **/
public interface ClientChannel extends MessageSender {
     
    
    /**
     * Add event listener.
     * 
     * @param webSocketEventListener the webSocketEventListener to add
     */
    public void addClientListener(ClientChannelListener listener);
    
    
    /**
     * Authenticate a user request.
     * 
     * @param request Request to authenticate.
     * 
     * @throws AuthenticationException if authentication was unsuccessful.
     */
    public String authenticate(AuthenticationProvider provider, HandshakeRequest request) throws AuthenticationException;


    public void onMessage(WebSocketClient client, byte[] byteArray);


    public void onMessage(WebSocketClient client, String message);


    public void registerClient(WebSocketClient webSocketClient);


    public void unregisterClient(WebSocketClient webSocketClient);


    public boolean hasClient(String clientId);
    
}

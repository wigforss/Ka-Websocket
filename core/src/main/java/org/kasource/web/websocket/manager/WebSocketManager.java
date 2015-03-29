package org.kasource.web.websocket.manager;


import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.channel.WebsocketMessageSender;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.internal.ClientListener;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.security.AuthenticationProvider;


/**
 * Web Socket Manager.
 * 
 * This interface is implemented by vendor specific WebSocketServlet implementations, in order
 * to get message and client event notifications.
 * 
 * @author rikardwi
 **/
public interface WebSocketManager extends WebsocketMessageSender {
     
    
    /**
     * Add event listener.
     * 
     * @param webSocketEventListener the webSocketEventListener to add
     */
    public void addClientListener(ClientListener listener);
    
    
    /**
     * Authenticate a user request.
     * 
     * @param request Request to authenticate.
     * 
     * @throws AuthenticationException if authentication was unsuccessful.
     */
    public String authenticate(AuthenticationProvider provider, HttpServletRequest request) throws AuthenticationException;


    public void onWebSocketMessage(WebSocketClient client, byte[] byteArray);


    public void onWebSocketMessage(WebSocketClient client, String readString);


    public void registerClient(WebSocketClient webSocketClient);


    public void unregisterClient(WebSocketClient webSocketClient);


    public boolean hasClient(String clientId);
    
}

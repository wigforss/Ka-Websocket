package org.kasource.web.websocket.channel;

import javax.servlet.http.HttpServletRequest;

import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.protocol.ProtocolHandler;

/**
 * Listens to client messages and events. Used for internal purposes only.
 * 
 * Implemented by the ServerChannel Implementation.
 * 
 * @author rikardwi
 **/
public interface ClientChannelListener {
    
    /**
     * Called on client connect.
     * 
     * @param client The connecting client
     **/
    public void onConnect(WebSocketClient client, ParameterBinder parameterBinder);
    
    /**
     * Called on client disconnect.
     * 
     * @param client The disconnecting client
     **/
    public void onDisconnect(WebSocketClient client, ParameterBinder parameterBinder);
    
    /**
     * Called after authentication
     * 
     * @param username Name of the user which was authenticated.
     * @param error    null if authentication was successful, else the cause of the authentication fail.
     **/
    public void onAuthentication(String username, HttpServletRequest request, Throwable error);
    
    /**
     * Called when a text message has been received from a client.
     * 
     * @param data      Message.
     * @param clientId  Id of the client who sent the message.
     **/
    public void onMessage(WebSocketClient client, String message, ProtocolHandler<String> protocol, ParameterBinder parameterBinder);
    
    /**
     * Called when a binary message has been received from a client.
     * 
     * @param is        Binary message as an InputStream.
     * @param clientId  Id of the client who sent the message.
     **/
    public void onBinaryMessage(WebSocketClient client, byte[] message, ProtocolHandler<byte[]> protocol, ParameterBinder parameterBinder);
}

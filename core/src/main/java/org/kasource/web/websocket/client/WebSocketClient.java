package org.kasource.web.websocket.client;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.protocol.ProtocolHandler;

/**
 * WebSocketClient that one can send messages to.
 * 
 * @author rikardwi
 **/
public interface WebSocketClient {
   
    /**
     * Sends a text message
     * 
     * @param message message to send.
     **/
    public void sendMessageToSocket(String message);
    
    /**
     * Sends a binary message
     * 
     * @param message message to send.
     **/
    public void sendMessageToSocket(byte[] message);
    
    public void sendBinaryMessageToSocket(Object message);
    
    public void sendTextMessageToSocket(Object message);
    
    /**
     * @return the username
     */
    public String getUsername();


    /**
     * @return the Upgrdade (connection) request
     */
    public HttpServletRequest getUpgradeRequest();
    
    public ProtocolHandler<String> getTextProtocolHandler();
    
    public ProtocolHandler<byte[]> getBinaryProtocolHandler();
    
    public String getUrl();
    
    public String getSubProtocol();
    
    /**
     * @return the id
     */
    public String getId();
}

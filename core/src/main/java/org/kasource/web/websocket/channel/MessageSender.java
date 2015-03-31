package org.kasource.web.websocket.channel;

import java.io.IOException;

import org.kasource.web.websocket.channel.client.NoSuchWebSocketClient;



/**
 * Web Socket Message Sender.
 * 
 * Allows messages to be sent to Web Socket clients.
 * 
 * @author rikardwi
 **/
public interface MessageSender  {

    
    
    /**
     * Broadcasts a text message to all clients.
     * 
     * @param message Message Object to send, if no text protocol handler is used the toString() method will be used
     **/
    public void broadcast(Object message);
    
    
    /**
     * Broadcasts a binary message to all clients.
     * 
     * @param message Message to send, if no no binary protocol handler is used its expected to be byte[], InputStream or Reader.
     **/
    public void broadcastBinary(Object message);
    
  
    
    /**
     * Sends a text message to a specific client.
     * 
     * @param message Message to send.
     * @param recipient ID of the recipient
     * @param recipientType Type of recipient
     * 
     * @throws IOException if message could not be sent.
     * @throws NoSuchWebSocketClient if there is no connected client with the supplied clientId.
     **/
    public void sendMessageToUser(Object message, String username) throws IOException, NoSuchWebSocketClient; 
    
    
    public void sendMessageToClient(Object message, String clientId) throws IOException, NoSuchWebSocketClient;
    
    /**
     * Sends a binary message to a specific client.
     * 
     * @param message Message to send.
     * @param recipient ID of the recipient
     * @param recipientType Type of recipient
     * 
     * @throws IOException if message could not be sent.
     * @throws NoSuchWebSocketClient if there is no connected client with the supplied clientId.
     **/
    public void sendBinaryMessageToUser(Object message, String username) throws IOException, NoSuchWebSocketClient; 
    
    public void sendBinaryMessageToClient(Object message, String clientId) throws IOException, NoSuchWebSocketClient; 
   
    
}

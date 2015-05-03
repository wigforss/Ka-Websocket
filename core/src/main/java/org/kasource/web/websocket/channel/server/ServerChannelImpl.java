package org.kasource.web.websocket.channel.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.channel.ClientChannelListener;
import org.kasource.web.websocket.channel.MessageSender;
import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.event.AuthenticationFailedEvent;
import org.kasource.web.websocket.event.AuthenticationSuccessEvent;
import org.kasource.web.websocket.event.BinaryMessageEvent;
import org.kasource.web.websocket.event.BinaryObjectMessageEvent;
import org.kasource.web.websocket.event.ClientConnectionEvent;
import org.kasource.web.websocket.event.ClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.TextMessageEvent;
import org.kasource.web.websocket.event.TextObjectMessageEvent;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.protocol.XmlProtocolHandler;

/**
 * Web Socket Channel Implementation.
 * 
 * @author rikardwi
 **/
public class ServerChannelImpl implements ServerChannel, ClientChannelListener {
    private String url;
    private MessageSender sender;
    private Set<WebSocketEventListener> eventListeners = new HashSet<WebSocketEventListener>(); 
        
    /**
     * Constructor used for lazy initialization. 
     * 
     * Its expected that the initialize will be invoked before this
     * instance is used, otherwise an Exception will be thrown.
     **/
    public ServerChannelImpl() {
        
    }
    
    /**
     * Constructor used for immediate initialization.
     * 
     * @param sender WebSocket manager used to send messages with.
     **/
    public ServerChannelImpl(String url, ClientChannel sender) { 
        initialize(url, sender);
    }
    
   
    
    /**
     * Initialize this WebSocket.
     * 
     * @param manager WebSocketManager to use.
     */
    public void initialize(String url, ClientChannel manager) {
        this.url = url;
        this.sender = manager;
        manager.addClientListener(this);
    }
    
   


    /**
     * Send a message to the web socket client with clientId.
     * 
     * @param clientId  ID of the client to send message to.
     * @param message   Message to send.
     * 
     * @throws IOException If the message could not be sent.
     * @see org.kasource.web.websocket.channel.MessageSender.websocket.WebsocketMessageSender#sendMessage(java.lang.String, java.lang.String)
     */
    @Override
    public void sendMessageToClient(Object message, String clientId) throws IOException {
        if(sender != null) {
            sender.sendMessageToClient(message, clientId);
        }
        
    }
    
    @Override
    public void sendMessageToUser(Object message, String username) throws IOException {
        if(sender != null) {
            sender.sendMessageToUser(message, username);
        }
        
    }
    

    
 

    /**
     * Send a binary message to a specific client.
     * 
     * @param clientId ID of the client recipient.
     * @param message  Message to send.
     * 
     * @throws IOException If the message could not be sent.
     */
    @Override
    public void sendBinaryMessageToClient(Object message, String clientId) throws IOException {
        if(sender != null) {
            sender.sendBinaryMessageToClient(message, clientId);
        }
        
    }
    
    @Override
    public void sendBinaryMessageToUser(Object message, String username) throws IOException {
        if(sender != null) {
            sender.sendBinaryMessageToUser(message, username);
        }
        
    }
    
    


    /**
     * Handle in-bound text messages.
     * 
     * @param message Message received from web socket.
     **/
    @Override
    public void onMessage(WebSocketClient client, String message, ProtocolHandler<String> textProtocol, ParameterBinder parameterBinder) {
        if(textProtocol == null) {
            fireEvent(new TextMessageEvent(this, message, client, parameterBinder));
        } else {
            fireEvent(new TextObjectMessageEvent(this, message, client, textProtocol, parameterBinder));
        }
       
    }

    /**
     * Handle in-bound binary messages.
     * 
     * @param message Message received from web socket.
     **/
    @Override
    public void onBinaryMessage(WebSocketClient client, byte[] message, ProtocolHandler<byte[]> binaryProtocol, ParameterBinder parameterBinder) {
        if(binaryProtocol == null) {
            fireEvent(new BinaryMessageEvent(this, message, client, parameterBinder));
        } else {
            fireEvent(new BinaryObjectMessageEvent(this, message, client, binaryProtocol, parameterBinder));
        }
    }
    
    
    /**
     * Add a listener to this channel.
     * 
     * @param listener The listener to add.
     **/
    @Override
    public void addListener(WebSocketEventListener listener) {
        eventListeners.add(listener);      
    }

    

    /**
     * Returns the name of this web socket channel.
     * 
     * @return name of this web socket channel.
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * Handle in-bound connection event.
     * 
     * @param clientId             ID of the client that has connected.
     * @param connectionParameters Connection Properties on the client connection request.
     **/
    @Override
    public void onConnect(WebSocketClient client, ParameterBinder parameterBinder) {
        fireEvent(new ClientConnectionEvent(this, client, parameterBinder));
        
    }

    /**
     * Handle in-bound disconnect event.
     * 
     * @param clientId ID of the client that was disconnected.
     **/
    @Override
    public void onDisconnect(WebSocketClient client, ParameterBinder parameterBinder) {
        fireEvent(new ClientDisconnectedEvent(this, client, parameterBinder));
        
    }
    
    /**
     * Fires events to all listeners.
     * 
     * @param event Event to emit.
     **/
    private void fireEvent(WebSocketEvent event) {
        for (WebSocketEventListener listener : eventListeners) {
            listener.onWebSocketEvent(event);
        }
    }

    @Override
    public void onAuthentication(String username, HandshakeRequest request, Throwable error) {
       if(error == null) {
           fireEvent(new AuthenticationSuccessEvent(this, username, request));
       } else {
           fireEvent(new AuthenticationFailedEvent(this, username, request, error));
       }
        
    }

    @Override
    public void broadcast(Object message) {
        if(sender != null) {
            sender.broadcast(message);
        }
        
    }

    @Override
    public void broadcastBinary(Object message) {
        if(sender != null) {
            sender.broadcastBinary(message);
        }
        
    }

  

    
}

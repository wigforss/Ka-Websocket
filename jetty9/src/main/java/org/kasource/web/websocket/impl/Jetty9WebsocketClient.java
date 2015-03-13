package org.kasource.web.websocket.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class Jetty9WebsocketClient implements WebSocketClient {
    private static final Logger LOG = LoggerFactory.getLogger(Jetty9WebsocketClient.class);
    private static final long DEFAULT_IDLE_TIMEOUT = 30000;
    private WebSocketClientConfig clientConfig;
    private Session session;
    private ProtocolHandler<String> textProtocolHandler;
    private ProtocolHandler<byte[]> binaryProtocolHandler;
    
    public Jetty9WebsocketClient(WebSocketClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }
    
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        clientConfig.getManager().unregisterClient(this);
        session = null;
    }
 
    @OnWebSocketConnect
    public void onConnect(Session session) {
       session.setIdleTimeout(DEFAULT_IDLE_TIMEOUT);
       this.session = session;
       clientConfig.getManager().registerClient(this); 
    }
 
    @OnWebSocketMessage
    public void onTextMessage(String msg) {
        clientConfig.getManager().onWebSocketMessage(this, msg);
    }

    @OnWebSocketMessage
    public void onBinaryMessage(InputStream is) {
        try {
            clientConfig.getManager().onWebSocketMessage(this, IOUtils.toByteArray(is));
        } catch (IOException e) {
            LOG.debug("Could not receive message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
    }
    
    @Override
    public void sendMessageToSocket(String message) {
        try {
            session.getRemote().sendString(message);
        } catch (IOException e) {
            LOG.debug("Could not send message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
        
    }

    @Override
    public void sendMessageToSocket(byte[] message) {
        try {
            session.getRemote().sendBytes(ByteBuffer.wrap(message));
        } catch (IOException e) {
            LOG.debug("Could not send message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
        
    }
    
    @Override
    public void sendBinaryMessageToSocket(Object message) {
        if(binaryProtocolHandler == null) {
            throw new IllegalStateException("No binary handler configured for client");
        }
        sendMessageToSocket(binaryProtocolHandler.toMessage(message));
    }

    @Override
    public void sendTextMessageToSocket(Object message) {
        if (textProtocolHandler == null) {
            throw new IllegalStateException("No text handler configured for client");
        }
        sendMessageToSocket(textProtocolHandler.toMessage(message));
    }

    

    @Override
    public String getUsername() {
        return clientConfig.getUsername();
    }

    @Override
    public Map<String, String[]> getConnectionParameters() {
        return clientConfig.getConnectionParameters();
    }

    @Override
    public void setTextProtocolHandler(ProtocolHandler<String> protocolHandler) {
       this.textProtocolHandler = protocolHandler;
        
    }

    @Override
    public void setBinaryProtocolHandler(ProtocolHandler<byte[]> protocolHandler) {
        this.binaryProtocolHandler = protocolHandler;
        
    }

    @Override
    public ProtocolHandler<String> getTextProtocolHandler() {
        return textProtocolHandler;
    }

    @Override
    public ProtocolHandler<byte[]> getBinaryProtocolHandler() {
        return binaryProtocolHandler;
    }

    @Override
    public String getUrl() {
       
        return clientConfig.getUrl();
    }

    @Override
    public String getSubProtocol() {
        return clientConfig.getSubProtocol();
    }
    
    
    

    @Override
    public String getId() {
       return clientConfig.getClientId();
    }

    
}

package org.kasource.web.websocket.jsr356;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.Session;

import org.apache.commons.io.IOUtils;
import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.HandshakeRequestData;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketClientJsr356 extends Endpoint implements MessageHandler, WebSocketClient {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketClientJsr356.class);
    private Session session;
    private HandshakeRequestData handshakeRequestData;
    private ClientChannel clientChannel;
    private ProtocolHandler<byte[]> binaryProtocolHandler;
    private ProtocolHandler<String> textProtocolHandler;
    private String id;
    private WebSocketClientConfig clientConfig;
    
    public WebSocketClientJsr356(WebSocketClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.handshakeRequestData = clientConfig.getRequest();
        this.clientChannel = clientConfig.getManager();
        binaryProtocolHandler = clientConfig.getBinaryProtocolHandler();
        textProtocolHandler = clientConfig.getTextProtocolHandler();
        id = clientConfig.getClientId();
    }
    
    @Override
    public void onOpen(Session session, EndpointConfig config) {
       this.session = session;
       if (clientConfig.getAsyncSendTimeoutMillis() > 0) {
           session.getAsyncRemote().setSendTimeout(clientConfig.getAsyncSendTimeoutMillis());
       }
       if (clientConfig.getMaxBinaryMessageBufferSizeByte() > 0){
           session.setMaxBinaryMessageBufferSize(clientConfig.getMaxBinaryMessageBufferSizeByte());
       }
       if (clientConfig.getMaxSessionIdleTimeoutMillis() > 0) {
           session.setMaxIdleTimeout(clientConfig.getMaxSessionIdleTimeoutMillis());
       }
       if (clientConfig.getMaxTextMessageBufferSizeByte() > 0) {
           session.setMaxTextMessageBufferSize(clientConfig.getMaxTextMessageBufferSizeByte());
       }
      
       session.addMessageHandler(this);
       if (id == null || id.trim().isEmpty()) {
           id = session.getId();
       }
       clientChannel.registerClient(this);
    }
    
    public void onClose(Session session, CloseReason closeReason) {
        clientChannel.unregisterClient(this);
    }
    
    public void onError(Session session, Throwable thr) {
    }
    
    @OnMessage
    public void onTextMessage(String message) {
        clientChannel.onMessage(this, message);
    }
    
    @OnMessage
    public void onBinaryMessage(byte[] message) {
        clientChannel.onMessage(this, message);
    }

    @Override
    public ProtocolHandler<byte[]> getBinaryProtocolHandler() {
        return binaryProtocolHandler;
    }

    @Override
    public String getId() {
        return id;
    }

   
    @Override
    public ProtocolHandler<String> getTextProtocolHandler() {
        return textProtocolHandler;
    }

    @Override
    public HandshakeRequestData getUpgradeRequest() {
        return handshakeRequestData;
    }

  

    @Override
    public String getUsername() {
       return clientConfig.getUsername();
    }

    
    
    private void sendMessageToSocket(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            LOG.debug("Could not send message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
        
    }

    
    private void sendMessageToSocket(byte[] message) {
        try {
            session.getBasicRemote().sendBinary(ByteBuffer.wrap(message));
        } catch (IOException e) {
            LOG.debug("Could not send message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
        
    }
    
    @Override
    public void sendBinaryMessageToSocket(Object message) {
        if (binaryProtocolHandler == null) {
            if (message instanceof byte[]) {
                sendMessageToSocket((byte[]) message);
            } else if (message instanceof InputStream) {
                try {
                    sendMessageToSocket(IOUtils.toByteArray((InputStream) message));
                } catch(IOException e) {
                    LOG.debug("Could not send message to socket " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
                }
            } else if (message instanceof Reader) {
                try {
                    sendMessageToSocket(IOUtils.toByteArray((Reader) message));
                } catch(IOException e) {
                    LOG.debug("Could not send message to socket " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
                }
            } else {
                sendMessageToSocket(message.toString().getBytes(Charset.forName("UTF-8")));
            }
        } else {
            sendMessageToSocket(binaryProtocolHandler.toMessage(message));
        }
        
    }

    @Override
    public void sendTextMessageToSocket(Object message) {
        if (textProtocolHandler == null) {
            sendMessageToSocket(message.toString());
        } else {
            sendMessageToSocket(textProtocolHandler.toMessage(message));
        }
        
    }

}

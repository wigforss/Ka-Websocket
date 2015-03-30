package org.kasource.web.websocket.impl;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.commons.io.IOUtils;
import org.kasource.web.websocket.client.UpgradeRequestData;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Tomcat7WebSocketClient extends StreamInbound implements WebSocketClient {
    private static final Logger LOG = LoggerFactory.getLogger(Tomcat7WebSocketClient.class);
    private WebSocketClientConfig clientConfig;
   
    private ProtocolHandler<String> textProtocolHandler;
    
    private ProtocolHandler<byte[]> binaryProtocolHandler;

    protected Tomcat7WebSocketClient(WebSocketClientConfig clientConfig) {
        textProtocolHandler = clientConfig.getTextProtocolHandler();
       
        binaryProtocolHandler = clientConfig.getBinaryProtocolHandler();
       
        this.clientConfig = clientConfig;
    }



    @Override
    protected void onBinaryData(InputStream is) throws IOException {
        
        clientConfig.getManager().onWebSocketMessage(this, IOUtils.toByteArray(is));

    }



    @Override
    protected void onTextData(Reader r) throws IOException {
        
        clientConfig.getManager().onWebSocketMessage(this, IOUtils.toString(r));
    }



    @Override
    protected void onOpen(WsOutbound outbound) {
        clientConfig.getManager().registerClient(this);
    }



    @Override
    protected void onClose(int status) {         
        clientConfig.getManager().unregisterClient(this);
    }
    
   
    

    @Override
    public void sendMessageToSocket(String message)  {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
        } catch (IOException e) {
            LOG.debug("Could not send message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
        
    }



    @Override
    public void sendMessageToSocket(byte[] message) {
        try {
            getWsOutbound().writeBinaryMessage(ByteBuffer.wrap(message));
        } catch (IOException e) {
            LOG.debug("Could not send message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
        
    }


   



    /**
     * @return the username
     */
    @Override
    public String getUsername() {
        return clientConfig.getUsername();
    }



    /**
     * @return the connectionParameters
     */
    @Override
    public UpgradeRequestData getUpgradeRequest() {
        return clientConfig.getRequest();
    }



    /**
     * @return the id
     */
    @Override
    public String getId() {
        return clientConfig.getClientId();
    }

    @Override
    public String getUrl() {
        return clientConfig.getUrl();
    }
    
    @Override
    public String getSubProtocol() {
        return clientConfig.getSubProtocol();
    }

    /**
     * @return the textProtocolHandler
     */
    @Override
    public ProtocolHandler<String> getTextProtocolHandler() {
        return textProtocolHandler;
    }


    /**
     * @return the binaryProtocolHandler
     */
    @Override
    public ProtocolHandler<byte[]> getBinaryProtocolHandler() {
        return binaryProtocolHandler;
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

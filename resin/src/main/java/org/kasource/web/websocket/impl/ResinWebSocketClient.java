package org.kasource.web.websocket.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.kasource.web.websocket.client.HandshakeRequestData;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.websocket.WebSocketContext;
import com.caucho.websocket.WebSocketListener;

public class ResinWebSocketClient implements WebSocketListener, WebSocketClient  {
    private static final Logger LOG = LoggerFactory.getLogger(ResinWebSocketClient.class);
    private  WebSocketClientConfig clientConfig;
  
    private WebSocketContext context;
    private ProtocolHandler<String> textProtocolHandler;
    private ProtocolHandler<byte[]> binaryProtocolHandler;
    
    public ResinWebSocketClient(WebSocketClientConfig clientConfig) {
       this.clientConfig = clientConfig;
       textProtocolHandler = clientConfig.getTextProtocolHandler();
       binaryProtocolHandler = clientConfig.getBinaryProtocolHandler();
    }



    @Override
    public void onClose(WebSocketContext context) throws IOException {
       
    }



    @Override
    public void onDisconnect(WebSocketContext context) throws IOException {
        clientConfig.getManager().unregisterClient(this);
    }



    @Override
    public void onReadBinary(WebSocketContext context, InputStream in) throws IOException {
        clientConfig.getManager().onMessage(this, IOUtils.toByteArray(in));
       

    }



    @Override
    public void onReadText(WebSocketContext context, Reader reader) throws IOException {
        clientConfig.getManager().onMessage(this, IOUtils.toByteArray(reader));

    }



    @Override
    public void onStart(WebSocketContext context) throws IOException {
        this.context = context;
        clientConfig.getManager().registerClient(this);
    }



    @Override
    public void onTimeout(WebSocketContext context) throws IOException {
      

    }
    
   
    public void sendMessage(String message) throws IOException  {
        BufferedWriter out = new BufferedWriter(context.startTextMessage());
        out.append(message);
        out.close();
    }

    
    public void sendBinaryMessage(byte[] message) throws IOException  {
        BufferedOutputStream out = new BufferedOutputStream(context.startBinaryMessage());
        out.write(message);
        out.close();
    }
   
    private void sendMessageToSocket(String message) {
        try {
            sendMessage(message);
        } catch (IOException e) {
        }
        
    }
    
    private void sendMessageToSocket(byte[] message) {
        try {
            sendBinaryMessage(message);
        } catch (IOException e) {
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
    public HandshakeRequestData getUpgradeRequest() {
        return clientConfig.getRequest();
    }



    /**
     * @return the id
     */
    @Override
    public String getId() {
        return clientConfig.getClientId();
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

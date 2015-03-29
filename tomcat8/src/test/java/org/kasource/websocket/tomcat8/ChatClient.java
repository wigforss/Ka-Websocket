package org.kasource.websocket.tomcat8;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class ChatClient {
    private Session userSession;
    private MessageHandler messageHandler;
 
    public ChatClient(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }
    
    public void connect(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider
                    .getWebSocketContainer();
            userSession = container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }
 
 
   
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
 
    
    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}

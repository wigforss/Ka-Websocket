package org.kasource.web.websocket.example;

import org.kasource.web.websocket.annotations.Broadcast;
import org.kasource.web.websocket.annotations.OnClientConnected;
import org.kasource.web.websocket.annotations.OnClientDisconnected;
import org.kasource.web.websocket.annotations.OnMessage;
import org.kasource.web.websocket.annotations.Username;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.config.annotation.AllowedOrigin;
import org.kasource.web.websocket.config.annotation.Authenticate;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;

@WebSocket("/chat/*")
@AllowedOrigin({"http://localhost:8080", "localhost:8080"})
@Authenticate(PassthroughAutenticationProvider.class)
public class AnnotatedChatServer {
    @OnMessage
    @Broadcast
    public String recieveMessage(String message, @Username String username) {
        return username + " says: " + message;      
    }
    
    @OnClientConnected
    @Broadcast
    public String onClientConnect(WebSocketClient client, @Username String username) {
        client.sendTextMessageToSocket("Welcome " + username);       
        return username + " joined the conversation.";
    }
    
    @OnClientDisconnected
    @Broadcast
    public String onClientDisconnect(@Username String username) {
       return username + " left the conversation.";
    }
}

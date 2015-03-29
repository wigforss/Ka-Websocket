package org.kasource.web.websocket.dropwizard.example;

import org.kasource.web.websocket.annotations.Broadcast;
import org.kasource.web.websocket.annotations.OnClientConnected;
import org.kasource.web.websocket.annotations.OnClientDisconnected;
import org.kasource.web.websocket.annotations.OnMessage;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.config.annotation.WebSocket;

@WebSocket("/chat/*")
public class ChatServer {
    @OnMessage
    @Broadcast
    public String recieveMessage(String message, String username) {
        return username + " says: " + message;      
    }
    
    @OnClientConnected
    @Broadcast
    public String onClientConnect(WebSocketClient client, String username) {
        client.sendTextMessageToSocket("Welcome " + username);       
        return username + " joined the conversation.";
    }
    
    @OnClientDisconnected
    @Broadcast
    public String onClientDisconnect(String username) {
       return username + " left the conversation.";
    }
}

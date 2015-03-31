package org.kasource.web.websocket.guice.example.web;



import java.io.IOException;

import org.kasource.web.websocket.annotations.Broadcast;
import org.kasource.web.websocket.annotations.ClientId;
import org.kasource.web.websocket.annotations.OnClientConnected;
import org.kasource.web.websocket.annotations.OnClientDisconnected;
import org.kasource.web.websocket.annotations.OnMessage;
import org.kasource.web.websocket.annotations.Username;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.id.ClientIdGeneratorImpl;
import org.kasource.web.websocket.config.annotation.Authenticate;
import org.kasource.web.websocket.config.annotation.DefaultTextProtocol;
import org.kasource.web.websocket.config.annotation.GenerateId;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;

import com.google.inject.Singleton;

@Singleton
@WebSocket("/chat")
@Authenticate(PassthroughAutenticationProvider.class)
@DefaultTextProtocol(JsonProtocolHandler.class)
@GenerateId(ClientIdGeneratorImpl.class)
public class ChatServer {

    @OnMessage
    @Broadcast
    public String onMessage(String message, @ClientId String clientId) {
        return clientId + " says: " + message;
    }
    
    @OnClientConnected
    @Broadcast
    public String onClientConnect(WebSocketClient client, @ClientId String clientId, @Username String username)  {
        client.sendTextMessageToSocket("Welcome " + username);
        return clientId + " joined the conversation.";
    }
    
    @OnClientDisconnected
    @Broadcast
    public String onClientDisconnect(@ClientId String clientId) {
        return clientId + " left the conversation.";
    }
    
}

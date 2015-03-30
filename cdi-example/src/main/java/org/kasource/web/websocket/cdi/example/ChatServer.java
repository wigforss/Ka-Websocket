package org.kasource.web.websocket.cdi.example;

import java.io.IOException;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.kasource.web.websocket.channel.NoSuchWebSocketClient;
import org.kasource.web.websocket.client.RecipientType;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.event.extension.BroadcastWebSocketTextMessageEvent;
import org.kasource.web.websocket.event.extension.SendWebSocketTextMessageEvent;

@Startup
@ApplicationScoped
public class ChatServer {
    
    @Inject @Any 
    private Event<SendWebSocketTextMessageEvent> sendEvent;
    
    @Inject @Any 
    private Event<BroadcastWebSocketTextMessageEvent> broadcastEvent;
    
    public ChatServer() {
        System.out.println("ChatServer created");
    }
    
    public void onClientConnect(@Observes @Chat WebSocketClientConnectionEvent event) throws NoSuchWebSocketClient, IOException {
        sendEvent.fire(new SendWebSocketTextMessageEvent(event, "Welcome " + event.getClient().getId(), RecipientType.CLIENT_ID));
        broadcastEvent.fire(new BroadcastWebSocketTextMessageEvent(event, event.getClient().getId() + " left the conversation."));
    }
    
    public void onClientDisconnect(@Observes @Chat WebSocketClientDisconnectedEvent event) {
        broadcastEvent.fire(new BroadcastWebSocketTextMessageEvent(event, event.getClient().getId() + " left the conversation."));
    }
    
    public void onMessage(@Observes @Chat WebSocketTextMessageEvent event) {
        broadcastEvent.fire(new BroadcastWebSocketTextMessageEvent(event, event.getClient().getId() + " says: " + event.getMessage()));
    }
    
    
    
}
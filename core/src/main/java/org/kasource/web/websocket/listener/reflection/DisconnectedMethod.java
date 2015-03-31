package org.kasource.web.websocket.listener.reflection;

import java.lang.reflect.Method;

import org.kasource.web.websocket.annotations.Broadcast;
import org.kasource.web.websocket.event.ClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisconnectedMethod implements WebSocketEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(DisconnectedMethod.class);
    private Object listener;
    private Method method;
    private boolean broadcastResponse;

    
    public DisconnectedMethod(Object listener, Method method) {
        if (method.isAccessible()) {
            throw new IllegalArgumentException("WebSocket Connection Listener method must be public");
        }
        
        
       
        this.method = method;
        this.listener = listener;
        
        this.broadcastResponse = method.isAnnotationPresent(Broadcast.class);
    }
    
    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
       if (event instanceof ClientDisconnectedEvent) {
           try {
               Object response = invokeMethod((ClientDisconnectedEvent) event);
               if (response != null) {
                   if (broadcastResponse) {
                       event.getSource().broadcast(response);
                   } else {
                       ((ClientDisconnectedEvent) event).getClient().sendTextMessageToSocket(response);
                   }
               }
           } catch (Exception e) {
               LOG.error("Could not invoke " + method, e);
            } 
       }
        
    }
    
    private Object invokeMethod(ClientDisconnectedEvent event) throws Exception {
       
        return method.invoke(listener, event.getParameterBinder().bindParameters(method, event, event.getSource(), event.getClient()));
        
    }

}

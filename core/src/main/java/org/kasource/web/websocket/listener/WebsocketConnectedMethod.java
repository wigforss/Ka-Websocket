package org.kasource.web.websocket.listener;

import java.lang.reflect.Method;

import org.kasource.web.websocket.annotations.Broadcast;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketConnectedMethod implements WebSocketEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebsocketConnectedMethod.class);
    private Object listener;
    private Method method;
    private boolean broadcastResponse;

    
    public WebsocketConnectedMethod(Object listener, Method method) {
        if (method.isAccessible()) {
            throw new IllegalArgumentException("WebSocket Connection Listener method must be public");
        }
       
        
        this.method = method;
        this.listener = listener;
        
        this.broadcastResponse = method.isAnnotationPresent(Broadcast.class);
    }
    
    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
       if (event instanceof WebSocketClientConnectionEvent) {
           try {
               Object response = invokeMethod((WebSocketClientConnectionEvent) event);
               if (response != null) {
                   if (broadcastResponse) {
                       event.getSource().broadcast(response);
                   } else {
                       ((WebSocketClientConnectionEvent) event).getClient().sendTextMessageToSocket(response);
                   }
               }
           } catch (Exception e) {
               LOG.error("Could not invoke " + method, e);
            } 
       }
        
    }
    
    private Object invokeMethod(WebSocketClientConnectionEvent event) throws Exception {
       return  method.invoke(listener, event.getParameterBinder().bindParameters(method, event, event.getUsername(), event.getSource(), event.getClient()));
    }

}

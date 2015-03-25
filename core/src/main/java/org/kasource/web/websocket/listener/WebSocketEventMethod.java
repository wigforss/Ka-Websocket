package org.kasource.web.websocket.listener;



import java.lang.reflect.Method;

import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.annotations.Broadcast;
import org.kasource.web.websocket.event.WebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketClientEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebSocketEventMethod implements WebSocketEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketEventMethod.class);
    private Object listener;
    private Method method;
    private boolean broadcastResponse;
   
    private ParameterBinder parameterBinder = new ParameterBinder();
    public WebSocketEventMethod(Object listener, Method method) {
        if (method.isAccessible()) {
            throw new IllegalArgumentException("WebSocket Event Listener method must be public");
        }
       
       
        this.method = method;
        this.listener = listener;
       
        this.broadcastResponse = method.isAnnotationPresent(Broadcast.class);
    }



    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        try {
                ParameterBinder binder = parameterBinder;
                if (event instanceof WebSocketClientEvent) {
                    binder = ((WebSocketClientEvent) event).getParameterBinder();
                }
                Object response = method.invoke(listener, binder.bindParameters(method, event, event.getSource()));
                if (response != null) {
                    if (event instanceof WebSocketTextMessageEvent) {
                        if (broadcastResponse) {
                            event.getSource().broadcast(response);
                        } else {
                            ((WebSocketTextMessageEvent) event).getClient().sendTextMessageToSocket(response); 
                        }
                    } else if (event instanceof WebSocketBinaryMessageEvent) {
                        if (broadcastResponse) {
                            event.getSource().broadcastBinary(response);
                        } else {
                            ((WebSocketBinaryMessageEvent) event).getClient().sendBinaryMessageToSocket(response); 
                        }
                    } else if (event instanceof WebSocketClientConnectionEvent) {
                        if (broadcastResponse) {
                            event.getSource().broadcast(response);
                        } else {
                            ((WebSocketClientConnectionEvent) event).getClient().sendTextMessageToSocket(response); 
                        }
                    } else if (event instanceof WebSocketClientDisconnectedEvent) {
                        if (broadcastResponse) {
                            event.getSource().broadcast(response);
                        } else {
                            ((WebSocketClientDisconnectedEvent) event).getClient().sendTextMessageToSocket(response); 
                        }
                    }
                }
            
        } catch (Exception e) {
           LOG.error("Could not invoke " + method, e);
        } 
        
    }


   
}

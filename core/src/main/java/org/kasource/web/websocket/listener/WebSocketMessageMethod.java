package org.kasource.web.websocket.listener;



import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.kasource.web.websocket.annotations.Broadcast;
import org.kasource.web.websocket.annotations.Payload;
import org.kasource.web.websocket.event.WebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.WebSocketBinaryObjectMessageEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.event.WebSocketTextObjectMessageEvent;
import org.kasource.web.websocket.protocol.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebSocketMessageMethod implements WebSocketEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketMessageMethod.class);
    
    private Object listener;
    private Method method;
    private boolean broadcastResponse;
    
    private Class<?> payloadType;
    
    public WebSocketMessageMethod(Object listener, Method method) {
        if (method.isAccessible()) {
            throw new IllegalArgumentException("WebSocket Message Listener method must be public");
        }
       
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.getAnnotation(Payload.class) != null) {
                if (payloadType != null) {
                    throw new IllegalStateException("Only one parameter in " + method + " can be annotated with @" + Payload.class);
                } else {
                    payloadType = parameter.getType();
                }
            }
        }
        
       
        this.method = method;
        this.listener = listener;
       
        this.broadcastResponse = method.isAnnotationPresent(Broadcast.class);
    }

    
    private Object onTextMessage(WebSocketTextMessageEvent messageEvent) {
       if (messageEvent instanceof WebSocketTextObjectMessageEvent) {
           return invokeTextMessageMethodConverted((WebSocketTextObjectMessageEvent) messageEvent);
       } else {
           return invokeTextMessageMethod(messageEvent);
       }
       
    }
    
    private Object invokeTextMessageMethod(WebSocketTextMessageEvent messageEvent) {
       
            try {
                return method.invoke(listener,messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessage(), messageEvent.getSource(), messageEvent.getClient(), messageEvent.getUsername()));
            } catch (Exception e) {
                throw new IllegalStateException("Could not invoke " + method, e);
            } 
        
    }
    
    private Object invokeTextMessageMethodConverted(WebSocketTextObjectMessageEvent messageEvent) {
        try {
            if (payloadType != null) {
                return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessageAsObject(payloadType), messageEvent.getSource(), messageEvent.getClient(), messageEvent.getUsername()));
            } else {
                return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessage(), messageEvent.getSource(), messageEvent.getClient(), messageEvent.getUsername()));
            }
        } catch(ConversionException e){
            throw new IllegalStateException("Could not convert " + messageEvent.getMessage() + " to " + payloadType);
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke " + method, e);
        }        
    }

    
    private Object onBinaryMessage(WebSocketBinaryMessageEvent messageEvent) {
        if (messageEvent instanceof WebSocketBinaryObjectMessageEvent) {
            return invokeBinaryMessageMethodConverted((WebSocketBinaryObjectMessageEvent) messageEvent);
        } else {
            return invokeBinaryMessageMethod(messageEvent);
        }
        
    }

    private Object invokeBinaryMessageMethod(WebSocketBinaryMessageEvent messageEvent) {
       
        try {
            byte[] data =  messageEvent.getMessage();
            return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, Arrays.asList(data).toArray(), messageEvent.getSource(), messageEvent.getClient(), messageEvent.getUsername()));
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke " + method, e);
        } 
             
    }
    
    private Object invokeBinaryMessageMethodConverted(WebSocketBinaryObjectMessageEvent messageEvent) {
        try {
            if (payloadType != null) {
                return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessageAsObject(payloadType), messageEvent.getSource(), messageEvent.getClient(), messageEvent.getUsername()));
            } else {
                return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessage(), messageEvent.getSource(), messageEvent.getClient(), messageEvent.getUsername()));
            }
        } catch(ConversionException e){
            throw new IllegalStateException("Could not convert " + messageEvent.getMessage() + " to " + payloadType);
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke " + method, e);
        } 
        
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {   
        try {
            if (event instanceof WebSocketTextMessageEvent) {
               Object response = onTextMessage((WebSocketTextMessageEvent) event);
               if (response != null) {
                   if (broadcastResponse) {
                       event.getSource().broadcast(response);
                   } else {
                       ((WebSocketTextMessageEvent) event).getClient().sendTextMessageToSocket(response);
                   }
               }
            } else if (event instanceof WebSocketBinaryMessageEvent) {
               Object response = onBinaryMessage((WebSocketBinaryMessageEvent) event);
               if (response != null) {
                   if (broadcastResponse) {
                       event.getSource().broadcastBinary(response);
                   } else {
                       ((WebSocketBinaryMessageEvent) event).getClient().sendBinaryMessageToSocket(response);
                   }
               }
            }
        } catch (RuntimeException e){
            LOG.warn(e.getMessage(), e);
        }
        
    }



   
}

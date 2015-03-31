package org.kasource.web.websocket.listener.reflection;



import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.kasource.web.websocket.annotations.Broadcast;
import org.kasource.web.websocket.annotations.Payload;
import org.kasource.web.websocket.event.BinaryMessageEvent;
import org.kasource.web.websocket.event.BinaryObjectMessageEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.TextMessageEvent;
import org.kasource.web.websocket.event.TextObjectMessageEvent;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.protocol.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageMethod implements WebSocketEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(MessageMethod.class);
    
    private Object listener;
    private Method method;
    private boolean broadcastResponse;
    
    private Class<?> payloadType;
    
    public MessageMethod(Object listener, Method method) {
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

    
    private Object onTextMessage(TextMessageEvent messageEvent) {
       if (messageEvent instanceof TextObjectMessageEvent) {
           return invokeTextMessageMethodConverted((TextObjectMessageEvent) messageEvent);
       } else {
           return invokeTextMessageMethod(messageEvent);
       }
       
    }
    
    private Object invokeTextMessageMethod(TextMessageEvent messageEvent) {
       
            try {
                return method.invoke(listener,messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessage(), messageEvent.getSource(), messageEvent.getClient()));
            } catch (Exception e) {
                throw new IllegalStateException("Could not invoke " + method, e);
            } 
        
    }
    
    private Object invokeTextMessageMethodConverted(TextObjectMessageEvent messageEvent) {
        try {
            if (payloadType != null) {
                return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessageAsObject(payloadType), messageEvent.getSource(), messageEvent.getClient()));
            } else {
                return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessage(), messageEvent.getSource(), messageEvent.getClient()));
            }
        } catch(ConversionException e){
            throw new IllegalStateException("Could not convert " + messageEvent.getMessage() + " to " + payloadType);
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke " + method, e);
        }        
    }

    
    private Object onBinaryMessage(BinaryMessageEvent messageEvent) {
        if (messageEvent instanceof BinaryObjectMessageEvent) {
            return invokeBinaryMessageMethodConverted((BinaryObjectMessageEvent) messageEvent);
        } else {
            return invokeBinaryMessageMethod(messageEvent);
        }
        
    }

    private Object invokeBinaryMessageMethod(BinaryMessageEvent messageEvent) {
       
        try {
            byte[] data =  messageEvent.getMessage();
            return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, Arrays.asList(data).toArray(), messageEvent.getSource(), messageEvent.getClient()));
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke " + method, e);
        } 
             
    }
    
    private Object invokeBinaryMessageMethodConverted(BinaryObjectMessageEvent messageEvent) {
        try {
            if (payloadType != null) {
                return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessageAsObject(payloadType), messageEvent.getSource(), messageEvent.getClient()));
            } else {
                return method.invoke(listener, messageEvent.getParameterBinder().bindParameters(method, messageEvent, messageEvent.getMessage(), messageEvent.getSource(), messageEvent.getClient()));
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
            if (event instanceof TextMessageEvent) {
               Object response = onTextMessage((TextMessageEvent) event);
               if (response != null) {
                   if (broadcastResponse) {
                       event.getSource().broadcast(response);
                   } else {
                       ((TextMessageEvent) event).getClient().sendTextMessageToSocket(response);
                   }
               }
            } else if (event instanceof BinaryMessageEvent) {
               Object response = onBinaryMessage((BinaryMessageEvent) event);
               if (response != null) {
                   if (broadcastResponse) {
                       event.getSource().broadcastBinary(response);
                   } else {
                       ((BinaryMessageEvent) event).getClient().sendBinaryMessageToSocket(response);
                   }
               }
            }
        } catch (RuntimeException e){
            LOG.warn(e.getMessage(), e);
        }
        
    }



   
}

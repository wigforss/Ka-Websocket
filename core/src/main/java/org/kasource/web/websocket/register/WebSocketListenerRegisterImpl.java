package org.kasource.web.websocket.register;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.annotations.OnClientConnected;
import org.kasource.web.websocket.annotations.OnClientDisconnected;
import org.kasource.web.websocket.annotations.OnMessage;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.listener.WebSocketBinaryMessageListener;
import org.kasource.web.websocket.listener.WebSocketClientConnectionListener;
import org.kasource.web.websocket.listener.WebSocketClientDisconnectedListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.listener.WebSocketTextMessageListener;
import org.kasource.web.websocket.listener.impl.WebSocketBinaryMessageHandler;
import org.kasource.web.websocket.listener.impl.WebSocketClientConnectedHandler;
import org.kasource.web.websocket.listener.impl.WebSocketClientDisconnectedHandler;
import org.kasource.web.websocket.listener.impl.WebSocketTextMessageHandler;
import org.kasource.web.websocket.listener.reflection.WebSocketMessageMethod;
import org.kasource.web.websocket.listener.reflection.WebsocketConnectedMethod;
import org.kasource.web.websocket.listener.reflection.WebsocketDisconnectedMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation that registers listeners to web socket events.
 * 
 * Both annotation and interface configuration is supported, @See WebSocketListenerRegister.
 * 
 * @author rikardwi
 **/
public class WebSocketListenerRegisterImpl implements WebSocketListenerRegister {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketListenerRegisterImpl.class); 
    
    private WebSocketChannelFactory webSocketFactory;
    
    public WebSocketListenerRegisterImpl(ServletContext servletContext) {
        
        this.webSocketFactory = (WebSocketChannelFactory) servletContext.getAttribute(WebSocketChannelFactory.class.getName());;
    }
    
    /**
     * Registers the listener object if its configuration is applicable for 
     * Web Socket listening.
     * 
     * @param listener Object to inspect and register for event listener if properly configured.
     * 
     * @throws IllegalArgumentException if any annotated listener method does not contain the correct method signature.
     **/
    @Override
    public void registerListener(Object listener) throws IllegalArgumentException {
        WebSocketListener listenerAnnotation = listener.getClass().getAnnotation(WebSocketListener.class);
        String url = null;
        if (listenerAnnotation == null) {
            // Find out if the WebSocketListenerRegistration is implemented
            if (listener instanceof WebSocketListenerRegistration) {
                url = ((WebSocketListenerRegistration) listener).getWebSocketChannelName();
            } else if (listener.getClass().isAnnotationPresent(WebSocket.class)) {
                url = listener.getClass().getAnnotation(WebSocket.class).value();
            }
        } else {
            url = listenerAnnotation.value();
        }
        if (url != null) {
            registerListener(listener, url);
        }
    }
    
    /**
     * Registers the listener object to the WebSocketChannel named webSocketChannelName if its configuration is applicable for 
     * Web Socket listening.
     * 
     * @param listener Object to inspect and register for event listener if properly configured.
     * @param url URL of the WebSocketChannel to register the listener at.
     * 
     * @throws IllegalArgumentException if any annotated listener method does not contain the correct method signature.
     **/
    private void registerListener(Object listener, String url) throws IllegalArgumentException {
        if (WebSocketEventListener.class.isAssignableFrom(listener.getClass()))  {
            webSocketFactory.listenTo(url, (WebSocketEventListener) listener);   
        }
        if (WebSocketBinaryMessageListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(url, new WebSocketBinaryMessageHandler((WebSocketBinaryMessageListener) listener));   
        }
        if (WebSocketTextMessageListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(url, new WebSocketTextMessageHandler((WebSocketTextMessageListener) listener));   
        }
        if (WebSocketClientConnectionListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(url, new WebSocketClientConnectedHandler((WebSocketClientConnectionListener) listener));   
        }
        if (WebSocketClientDisconnectedListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(url, new WebSocketClientDisconnectedHandler((WebSocketClientDisconnectedListener) listener));   
        }
        registerAnnotatedMethods(listener, url);
    }
    
    
    /**
     * Finds and registers all eligible annotated methods as separate listeners.
     * 
     * @param listener               Listener object to register.
     * @param url   Name of the WebSocketChannel to register at.
     * @throws IllegalArgumentException if any annotated listener method does not contain the correct method signature.
     **/
    private void registerAnnotatedMethods(Object listener, String url) throws IllegalArgumentException {
        Method[] methods = listener.getClass().getMethods();
        for(Method method : methods) {
            if (url != null || method.isAnnotationPresent(WebSocketListener.class)) {
                WebSocketListener methodListerAnnotation = method.getAnnotation(WebSocketListener.class);
               
                if (methodListerAnnotation != null) {
                    url = methodListerAnnotation.value();
                }
                
                
                if (method.isAnnotationPresent(OnMessage.class)) { 
                    registerListener(url, new WebSocketMessageMethod(listener, method));
                } else if (method.isAnnotationPresent(OnClientConnected.class)) {
                    registerListener(url, new WebsocketConnectedMethod(listener, method));
                } else if (method.isAnnotationPresent(OnClientDisconnected.class)) {
                    registerListener(url, new WebsocketDisconnectedMethod(listener, method));               
                } 
            }
        }
        
    }
    
    
    
    
    
    
    
    private void registerListener(String channelName, WebSocketEventListener listener) {         
        webSocketFactory.listenTo(channelName, listener);   
    }
    
   
}

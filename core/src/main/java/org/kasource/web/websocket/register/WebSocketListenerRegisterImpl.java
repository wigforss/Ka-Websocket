package org.kasource.web.websocket.register;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.annotations.OnClientConnected;
import org.kasource.web.websocket.annotations.OnClientDisconnected;
import org.kasource.web.websocket.annotations.OnMessage;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.listener.BinaryMessageListener;
import org.kasource.web.websocket.listener.ClientConnectionListener;
import org.kasource.web.websocket.listener.ClientDisconnectedListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.listener.TextMessageListener;
import org.kasource.web.websocket.listener.impl.BinaryMessageHandler;
import org.kasource.web.websocket.listener.impl.ClientConnectedHandler;
import org.kasource.web.websocket.listener.impl.ClientDisconnectedHandler;
import org.kasource.web.websocket.listener.impl.TextMessageHandler;
import org.kasource.web.websocket.listener.reflection.MessageMethod;
import org.kasource.web.websocket.listener.reflection.ConnectedMethod;
import org.kasource.web.websocket.listener.reflection.DisconnectedMethod;
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
    
    private ServerChannelFactory webSocketFactory;
    
    public WebSocketListenerRegisterImpl(ServletContext servletContext) {
        
        this.webSocketFactory = (ServerChannelFactory) servletContext.getAttribute(ServerChannelFactory.class.getName());;
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
                url = ((WebSocketListenerRegistration) listener).getWebSocketUrl();
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
        if (BinaryMessageListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(url, new BinaryMessageHandler((BinaryMessageListener) listener));   
        }
        if (TextMessageListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(url, new TextMessageHandler((TextMessageListener) listener));   
        }
        if (ClientConnectionListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(url, new ClientConnectedHandler((ClientConnectionListener) listener));   
        }
        if (ClientDisconnectedListener.class.isAssignableFrom(listener.getClass())) {
            webSocketFactory.listenTo(url, new ClientDisconnectedHandler((ClientDisconnectedListener) listener));   
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
                    registerListener(url, new MessageMethod(listener, method));
                } else if (method.isAnnotationPresent(OnClientConnected.class)) {
                    registerListener(url, new ConnectedMethod(listener, method));
                } else if (method.isAnnotationPresent(OnClientDisconnected.class)) {
                    registerListener(url, new DisconnectedMethod(listener, method));               
                } 
            }
        }
        
    }
    
    
    
    
    
    
    
    private void registerListener(String channelName, WebSocketEventListener listener) {         
        webSocketFactory.listenTo(channelName, listener);   
    }
    
   
}

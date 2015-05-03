package org.kasource.web.websocket.guice.registration;



import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.register.EndpointRegistrator;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.spi.InjectionListener;


public class WebSocketListenerInjectionListener implements InjectionListener<Object> {

  
    private WebSocketListenerRegister listenerRegister;
    private Deque<Object> listeners = new LinkedList<Object>();
    private EndpointRegistrator endpointRegistrator;
   
    @Override
    public void afterInjection(Object injectee) {
        if(listenerRegister == null) {
            listeners.add(injectee);     
        } else {
            if (injectee.getClass().isAnnotationPresent(WebSocket.class)) {
                endpointRegistrator.register(injectee.getClass());
            }
            listenerRegister.registerListener(injectee); 
        }
    }
        
    
    
   @Inject
    public void initialize(Injector injector, ServerChannelFactory factory, EndpointRegistrator endpointRegistrator) {   
        ServletContext servletContext = injector.getInstance(ServletContext.class);
        this.listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
        this.endpointRegistrator = endpointRegistrator;
        Object listener = null;
        while ((listener = listeners.pollFirst()) != null) {
            if (listener.getClass().isAnnotationPresent(WebSocket.class)) {
                endpointRegistrator.register(listener.getClass());
            }
            listenerRegister.registerListener(listener);          
        }
                
    }
    
   
}

package org.kasource.web.websocket.guice.registration;



import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;
import org.kasource.web.websocket.servlet.ServletRegistrator;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.spi.InjectionListener;


public class WebSocketListenerInjectionListener implements InjectionListener<Object> {

  
    private WebSocketListenerRegister listenerRegister;
    private Deque<Object> listeners = new LinkedList<Object>();
    private ServletRegistrator servletRegistrator;
   
    @Override
    public void afterInjection(Object injectee) {
        if(listenerRegister == null) {
            listeners.add(injectee);     
        } else {
            if (injectee.getClass().isAnnotationPresent(WebSocket.class)) {
                servletRegistrator.addServlet(injectee.getClass());
            }
            listenerRegister.registerListener(injectee); 
        }
    }
        
    
    
   @Inject
    public void initialize(Injector injector, ServerChannelFactory factory, ServletRegistrator servletRegistrator) {   
        ServletContext servletContext = injector.getInstance(ServletContext.class);
        this.listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
        this.servletRegistrator = servletRegistrator;
        Object listener = null;
        while ((listener = listeners.pollFirst()) != null) {
            if (listener.getClass().isAnnotationPresent(WebSocket.class)) {
                servletRegistrator.addServlet(listener.getClass());
            }
            listenerRegister.registerListener(listener);          
        }
                
    }
    
   
}

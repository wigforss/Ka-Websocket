package org.kasource.web.websocket.example;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.loader.AnnotatedEndpointConfigBuilder;
import org.kasource.web.websocket.register.EndpointRegistratorImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;


public class WebSocketEndpointLoader implements ServletContextAttributeListener {

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        if (event.getValue() instanceof WebSocketConfig) {
            new EndpointRegistratorImpl(event.getServletContext(), new AnnotatedEndpointConfigBuilder()).register(AnnotatedChatServer.class);
            WebSocketConfig config = (WebSocketConfig) event.getValue();
            WebSocketListenerRegister register = config.getListenerRegister();
            
            AnnotatedChatServer server = new AnnotatedChatServer();
            
            register.registerListener(server);
           
        }
        
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
       
        
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
       
        
    }

   

}

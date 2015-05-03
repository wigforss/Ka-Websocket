package org.kasource.web.websocket.register;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.config.loader.EndpointAnnotationConfigurationBuilder;
import org.kasource.web.websocket.jsr356.Jsr365Registrator;
import org.kasource.web.websocket.servlet.ServletRegistrator;

public class EndpointRegistratorImpl implements EndpointRegistrator {
    private ServletContext servletContext;
    private EndpointAnnotationConfigurationBuilder configurationBuilder;
    
    public EndpointRegistratorImpl(ServletContext servletContext, 
                                  EndpointAnnotationConfigurationBuilder configurationBuilder) {
        this.servletContext = servletContext;
        this.configurationBuilder = configurationBuilder;
    }

    @Override
    public void register(Class<?> webSocketPojo) {
        WebSocket websocket = webSocketPojo.getAnnotation(WebSocket.class);
        if (websocket == null) {
            throw new WebSocketConfigException(webSocketPojo + " must be annotated with " + WebSocket.class);
        }
        if (hasJsr365Support()) {
            new Jsr365Registrator(servletContext, configurationBuilder).register(webSocketPojo);
        } else {
            new ServletRegistrator(servletContext, configurationBuilder, resolveServlet()).register(webSocketPojo);
        }
        
    }
    
    private boolean hasJsr365Support() {
        return servletContext.getAttribute("javax.websocket.server.ServerContainer") != null;     
    }
    
   
    
    @SuppressWarnings("unchecked")
    private Class<? extends Servlet> resolveServlet() {
        Class <?> servletClass = null;
        try {
            servletClass = Class.forName("org.kasource.web.websocket.impl.WebsocketServletImpl");
        } catch (ClassNotFoundException e) {
           throw new IllegalStateException("Could not find any Websocket Servlet Implementation in classpath");
        } 
        try {
            return (Class<? extends Servlet>) servletClass;
        } catch (ClassCastException e) {
            throw new IllegalStateException(servletClass + " is not a " + Servlet.class + "!");
        }
    }
    
    

   
}

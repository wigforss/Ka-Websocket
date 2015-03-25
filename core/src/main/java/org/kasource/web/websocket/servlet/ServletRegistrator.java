package org.kasource.web.websocket.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.kasource.web.websocket.config.AnnotatedWebSocketServletConfigBuilder;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.config.annotation.WebSocket;

public class ServletRegistrator {
    
    private ServletContext servletContext;
   
    
    public ServletRegistrator(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    public void addServlet(Class<?> webocketPojo) { 
        WebSocket websocket = webocketPojo.getAnnotation(WebSocket.class);
        if (websocket == null) {
            throw new WebSocketConfigException(webocketPojo + " must be annotated with " + WebSocket.class);
        }
        String url = websocket.value();
        String name = "ka-websocket-" + url.replace("/", "_").replace("*", "-");
        AnnotatedWebSocketServletConfigBuilder builder = new AnnotatedWebSocketServletConfigBuilder();
        WebSocketServletConfig config =  builder.configure(webocketPojo);
        servletContext.setAttribute("config-" + name, config);
        
        ServletRegistration reg = servletContext.addServlet(name, resolve());
        reg.addMapping(url);
        
    }
    
    @SuppressWarnings("unchecked")
    public Class<? extends Servlet> resolve() {
        Class <?> servletClass = null;
        try {
            servletClass = Class.forName("org.kasource.web.websocket.impl.WebsocketServletImpl");
        } catch (ClassNotFoundException e) {
           throw new IllegalStateException("Could not find any WebsocketServlet Implementation i classpath");
        } 
        try {
            return (Class<? extends Servlet>) servletClass;
        } catch (ClassCastException e) {
            throw new IllegalStateException(servletClass + " is not a " + Servlet.class + "!");
        }
    }
}

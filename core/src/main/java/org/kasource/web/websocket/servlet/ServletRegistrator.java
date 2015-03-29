package org.kasource.web.websocket.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.config.loader.WebSocketServletAnnotationConfigurationBuilder;


public class ServletRegistrator {
    
    private ServletContext servletContext;
    private WebSocketServletAnnotationConfigurationBuilder configurationBuilder;
    
    public ServletRegistrator(ServletContext servletContext, WebSocketServletAnnotationConfigurationBuilder configurationBuilder) {
        this.servletContext = servletContext;
        this.configurationBuilder = configurationBuilder;
    }
    
    public void addServlet(Class<?> webocketPojo) { 
        WebSocket websocket = webocketPojo.getAnnotation(WebSocket.class);
        if (websocket == null) {
            throw new WebSocketConfigException(webocketPojo + " must be annotated with " + WebSocket.class);
        }
        String url = websocket.value();
        String name = "ka-websocket-" + url.replace("/", "_").replace("*", "-");
       
        WebSocketServletConfigImpl servletConfig =  configurationBuilder.configure(webocketPojo);
        servletConfig.setServletName(name);
        WebSocketConfig config = (WebSocketConfig) servletContext.getAttribute(WebSocketConfig.class.getName());
        config.registerServlet(servletConfig);
        
        
        ServletRegistration.Dynamic registration = servletContext.addServlet(name, resolve());
        

        registration.setLoadOnStartup(1);
        registration.addMapping(url);
        registration.setAsyncSupported(true);
      
        
      //  ServletRegistration reg = servletContext.addServlet(name, resolve());
      //  reg.addMapping(url);
        
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

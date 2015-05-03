package org.kasource.web.websocket.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.EndpointConfigImpl;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.config.loader.EndpointAnnotationConfigurationBuilder;
import org.kasource.web.websocket.register.EndpointRegistrator;


public class ServletRegistrator implements EndpointRegistrator {
    
    private ServletContext servletContext;
    private EndpointAnnotationConfigurationBuilder configurationBuilder;
    private Class<? extends Servlet> servletClass;
    
    public ServletRegistrator(ServletContext servletContext, 
                             EndpointAnnotationConfigurationBuilder configurationBuilder,
                             Class<? extends Servlet> servletClass) {
        this.servletContext = servletContext;
        this.configurationBuilder = configurationBuilder;
        this.servletClass = servletClass;
    }
    
    public void register(Class<?> webSocketPojo) { 
        WebSocket websocket = webSocketPojo.getAnnotation(WebSocket.class);
        if (websocket == null) {
            throw new WebSocketConfigException(webSocketPojo + " must be annotated with " + WebSocket.class);
        }
        String url = websocket.value();
        String name = "ka-websocket-" + url.replace("/", "_").replace("*", "-");
       
        EndpointConfigImpl servletConfig =  configurationBuilder.configure(webSocketPojo);
        servletConfig.setName(name);
        WebSocketConfig config = (WebSocketConfig) servletContext.getAttribute(WebSocketConfig.class.getName());
        config.registerEndpointConfig(servletConfig);
        
        
        ServletRegistration.Dynamic registration = servletContext.addServlet(name, servletClass);
        

        registration.setLoadOnStartup(1);
        registration.addMapping(url);
        registration.setAsyncSupported(true);
      
        
      //  ServletRegistration reg = servletContext.addServlet(name, resolve());
      //  reg.addMapping(url);
        
    }
    
    
}

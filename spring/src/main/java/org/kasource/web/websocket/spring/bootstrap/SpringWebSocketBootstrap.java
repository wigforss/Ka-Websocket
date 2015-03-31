package org.kasource.web.websocket.spring.bootstrap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;


public class SpringWebSocketBootstrap implements ServletContextAware, ServletContextAttributeListener  {

    
    private ServletContext servletContext;
    private ServerChannelFactory serverChannelFactory;
    private WebSocketConfig webSocketConfig;
    
    public SpringWebSocketBootstrap(WebSocketConfig webSocketConfig) {
         this.webSocketConfig = webSocketConfig;
    }
    
    public void configure() {
        
        servletContext.addListener(this);
        servletContext.setAttribute(WebSocketConfig.class.getName(), webSocketConfig);
    }

 
  
    @Override
    public void setServletContext(ServletContext servletContext) {
       this.servletContext = servletContext;
        
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        serverChannelFactory.addClientChannelFromAttribute(event.getName(), event.getValue());
        
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
       
        
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
     
        
    }

    /**
     * @param channelFactory the channelFactory to set
     */
    @Required
    public void setChannelFactory(ServerChannelFactory channelFactory) {
        this.serverChannelFactory = channelFactory;
    }   

   
}

package org.kasource.web.websocket.guice.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.ClientConfigImpl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;

@Singleton
public class GuiceKaWebSocketConfigurer implements ServletContextAttributeListener {
    private Injector injector;
    private ServerChannelFactory channelFactory;
    private WebSocketConfig config;
    private ServletContext servletContext;
    
    
    public  GuiceKaWebSocketConfigurer() { 
    }

    @Inject
    public void initialize(Injector injector, WebSocketConfig config, ServerChannelFactory channelFactory) {
        this.injector = injector;
        this.servletContext = injector.getInstance(ServletContext.class);
        this.channelFactory = channelFactory;
        this.config = config;
        configure();
    }
    
    public void configure() {
        
        servletContext.addListener(this);
        servletContext.setAttribute(WebSocketConfig.class.getName(), config);
       
        registerServletConfigs();
    }
    
   
    
    private void registerServletConfigs() {
        for (Key<?> bindingKey : injector.getBindings().keySet()) {
            if (ClientConfigImpl.class.isAssignableFrom(bindingKey.getTypeLiteral().getRawType())) {
                ClientConfigImpl servletConfig = (ClientConfigImpl) injector.getInstance(bindingKey);
                if(servletConfig.getServletName() != null) {
                    config.registerClientConfig(servletConfig);
                }
            }
            
        }
    }
    
    
    
    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        channelFactory.addClientChannelFromAttribute(event.getName(), event.getValue());
        
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
      
        
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
       
        
    }
    
    
}

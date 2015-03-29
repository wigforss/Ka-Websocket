package org.kasource.web.websocket.bootstrap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.kasource.web.websocket.channel.WebSocketChannelFactoryImpl;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.loader.DefaultWebSocketConfigLoader;
import org.kasource.web.websocket.config.loader.WebSocketConfigLoader;
import org.kasource.web.websocket.config.loader.WebSocketXmlConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstraps the WebSocket Framework.
 * 
 * @author rikardwi
 **/
public class WebSocketBootstrap implements ServletContextListener, ServletContextAttributeListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketBootstrap.class);
    private WebSocketChannelFactoryImpl factory;
    private WebSocketConfigImpl configuration;
   
    public WebSocketBootstrap() {
        
    }
    
    public WebSocketBootstrap(WebSocketConfigImpl configuration) {
        this.configuration = configuration;
    }   
    
    /**
     * Listener invoked when a new attribute has been added.
     * 
     * @param attributeEvent
     *            Event with the added attribute.
     **/
    @Override
    public void attributeAdded(ServletContextAttributeEvent attributeEvent) {
        if (factory != null) {
            factory.addWebSocketManagerFromAttribute(attributeEvent.getName(), attributeEvent.getValue());
        }
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scab) {
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent scab) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
           
            factory = new WebSocketChannelFactoryImpl();
            factory.initialize(event.getServletContext());
            loadAndpublishConfig(event.getServletContext());

        } catch (Exception e) {
            LOG.error("Could not load websocket configuration", e);
            throw new IllegalStateException("Could not initialize " + this.getClass(), e);
        }

    }

    private void loadAndpublishConfig(ServletContext servletContext) {
        String configLocation = servletContext.getInitParameter("webSocketConfigLocation");
        WebSocketConfigLoader configLoader = null;
        if (configLocation == null || configLocation.isEmpty()) {
             configLoader = new DefaultWebSocketConfigLoader(configuration, servletContext);
             LOG.info("No configuration location set. Falling back to default configuration");
        } else {
             configLoader = new WebSocketXmlConfigLoader(configLocation, servletContext);
        }
        WebSocketConfig config = configLoader.loadConfig();
        servletContext.setAttribute(WebSocketConfig.class.getName(), config);
        
    }
    
   

}

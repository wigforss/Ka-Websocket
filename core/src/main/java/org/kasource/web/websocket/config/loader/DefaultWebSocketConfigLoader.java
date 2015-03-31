package org.kasource.web.websocket.config.loader;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.client.ClientChannelRepositoryImpl;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;

public class DefaultWebSocketConfigLoader implements WebSocketConfigLoader {

    private ServletContext servletContext;
    private WebSocketConfigImpl configuration;
    
    public DefaultWebSocketConfigLoader(WebSocketConfigImpl configuration, ServletContext servletContext) {
        this.servletContext = servletContext;
        this.configuration = configuration;
    }
    
    @Override
    public WebSocketConfig loadConfig() {
        WebSocketConfigImpl config = new WebSocketConfigImpl();
        if (configuration != null) {
            config = configuration;
        }
        config.setServerChannelFactory((ServerChannelFactory) servletContext.getAttribute(ServerChannelFactory.class.getName()));
        config.setManagerRepository(new ClientChannelRepositoryImpl(servletContext));
        config.setListenerRegister(new WebSocketListenerRegisterImpl(servletContext));
        
        return config;
    }

}

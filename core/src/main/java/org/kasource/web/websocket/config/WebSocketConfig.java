package org.kasource.web.websocket.config;


import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.register.WebSocketListenerRegister;


public interface WebSocketConfig {
    
    public ClientConfig getClientConfig(String servletName);
    
    public ClientChannelRepository getClientChannelRepository();
    
    public ServerChannelFactory getServerChannelFactory();
    
    public WebSocketListenerRegister getListenerRegister();
    
    public void registerClientConfig(ClientConfigImpl clientConfig);
}

package org.kasource.web.websocket.config;


import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.register.WebSocketListenerRegister;


public interface WebSocketConfig {
    
    public EndpointConfig getEndpointConfig(String name);
    
    public ClientChannelRepository getClientChannelRepository();
    
    public ServerChannelFactory getServerChannelFactory();
    
    public WebSocketListenerRegister getListenerRegister();
    
    public void registerEndpointConfig(EndpointConfigImpl clientConfig);
}

package org.kasource.web.websocket.config;


import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.register.WebSocketListenerRegister;


public interface WebSocketConfig {
    
    public WebSocketServletConfig getServletConfig(String servletName);
    
    public WebSocketManagerRepository getManagerRepository();
    
    public WebSocketChannelFactory getChannelFactory();
    
    public WebSocketListenerRegister getListenerRegister();
    
    public void registerServlet(WebSocketServletConfigImpl servletConfig);
}

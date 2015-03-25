package org.kasource.web.websocket.config;

import java.util.Set;

import org.kasource.web.websocket.client.WebSocketClientBuilderFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.security.AuthenticationProvider;

public interface WebSocketServletConfig {
    boolean isDynamicAddressing();

    WebSocketClientBuilderFactory getClientBuilder(WebSocketManager manager);

    WebSocketManager getWebSocketManager(String url);

    AuthenticationProvider getAuthenticationProvider();
    
    ProtocolRepository getProtocolRepository();

    Set<String> getOriginWhitelist();
    
    boolean isValidOrigin(String origin);
    
    void merge(WebSocketServletConfig config);
    
    String getServletName();
    
    ClientIdGenerator getClientIdGenerator();

}

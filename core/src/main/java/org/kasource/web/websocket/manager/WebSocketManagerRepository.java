package org.kasource.web.websocket.manager;

import org.kasource.web.websocket.protocol.ProtocolRepository;

public interface WebSocketManagerRepository {
    public static final String ATTRIBUTE_PREFIX = WebSocketManager.class.getName() + "_";
        
    
    public WebSocketManager getWebSocketManager(String url);
    
}

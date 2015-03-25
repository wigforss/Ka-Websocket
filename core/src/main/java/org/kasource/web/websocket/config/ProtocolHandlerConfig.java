package org.kasource.web.websocket.config;

import java.util.Map;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public interface ProtocolHandlerConfig<T> {
    public ProtocolHandler<T> getDefaultProtocol();
    
    public Map<String, ProtocolHandler<T>> getProtocolHandlers();
    
  
}

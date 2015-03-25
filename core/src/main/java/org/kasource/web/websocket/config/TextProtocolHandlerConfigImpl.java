package org.kasource.web.websocket.config;

import java.util.Map;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public class TextProtocolHandlerConfigImpl implements ProtocolHandlerConfig<String>{
    private Map<String, ProtocolHandler<String>> protocolHandlers;
    private ProtocolHandler<String> defaultHandler;
 
    @Override
    public ProtocolHandler<String> getDefaultProtocol() {
        return defaultHandler;
    }
  

    /**
     * @param defaultHandler the defaultHandler to set
     */
    public void setDefaultProtocol(ProtocolHandler<String> defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    /**
     * @param handlers the handlers to set
     */
    public void setProtocolHandlers(Map<String, ProtocolHandler<String>> protocolHandlers) {
        this.protocolHandlers = protocolHandlers;
    }

    @Override
    public Map<String, ProtocolHandler<String>> getProtocolHandlers() {
        return protocolHandlers;
    }

    

}

package org.kasource.web.websocket.config.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.jaxb.Protocol;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class XmlTextProtocolHandlerConfig extends XmlAbstractProtocolHandlerConfig implements ProtocolHandlerConfig<String>{
   
    private Map<String, ProtocolHandler<String>> protocolHandlers = new HashMap<String, ProtocolHandler<String>>();
    private ProtocolHandler<String> defaultProtocol;
    
    public XmlTextProtocolHandlerConfig(org.kasource.web.websocket.config.xml.jaxb.ProtocolHandler config) {
      
        try {
            loadTextProtocols(config);
        } catch(Exception e) {
            throw new WebSocketConfigException("Could not load text protocols", e);
        }
    }

    @Override
    public ProtocolHandler<String> getDefaultProtocol() {
        return defaultProtocol;
    }

    
    
    private void loadTextProtocols(org.kasource.web.websocket.config.xml.jaxb.ProtocolHandler config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        defaultProtocol = getProtocol(config.getDefaultHandlerClass(), String.class);
        
        for(Protocol protocol : config.getProtocol()) {
            ProtocolHandler<String> protocolHandler = getProtocol(protocol.getHandlerClass(), String.class);
            protocolHandlers.put(protocol.getProtocol(), protocolHandler);
        }
    }

    @Override
    public Map<String, ProtocolHandler<String>> getProtocolHandlers() {
        return protocolHandlers;
    }
   
}

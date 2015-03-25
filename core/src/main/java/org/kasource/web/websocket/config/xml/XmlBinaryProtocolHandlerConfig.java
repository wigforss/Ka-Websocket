package org.kasource.web.websocket.config.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.jaxb.Protocol;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class XmlBinaryProtocolHandlerConfig extends XmlAbstractProtocolHandlerConfig implements ProtocolHandlerConfig<byte[]> {
    
    private Map<String, ProtocolHandler<byte[]>> protocolHandlers = new HashMap<String, ProtocolHandler<byte[]>>();
    private ProtocolHandler<byte[]> defaultProtocol;
    
    public XmlBinaryProtocolHandlerConfig(org.kasource.web.websocket.config.xml.jaxb.ProtocolHandler config) {
        
        try {
            loadBinaryProtocols(config);
        } catch(Exception e) {
            throw new WebSocketConfigException("Could not load binary protocols", e);
        }
    }
    
    @Override
    public ProtocolHandler<byte[]> getDefaultProtocol() {
        return defaultProtocol;
    }

    @Override
    public Map<String, ProtocolHandler<byte[]>> getProtocolHandlers() {
        return protocolHandlers;
    }
    

    private void loadBinaryProtocols(org.kasource.web.websocket.config.xml.jaxb.ProtocolHandler config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        defaultProtocol = getProtocol(config.getDefaultHandlerClass(), byte[].class);
        for (Protocol protocol : config.getProtocol()) {
            ProtocolHandler<byte[]> protocolHandler = getProtocol(protocol.getHandlerClass(), byte[].class);
            protocolHandlers.put(protocol.getProtocol(), protocolHandler);
        }
        
        
    }

   

    
   

}

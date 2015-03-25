package org.kasource.web.websocket.config.xml;

import java.util.ArrayList;
import java.util.List;

import org.kasource.web.websocket.config.xml.jaxb.Protocol;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public abstract class XmlAbstractProtocolHandlerConfig {

    
    
    @SuppressWarnings("unchecked")
    protected <T> ProtocolHandler<T> getProtocol(String protcolHandler, Class<T> ofType) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (protcolHandler != null && !protcolHandler.isEmpty()) {
            Class<?> handlerClass = Class.forName(protcolHandler);
            ProtocolHandler<T> handler = (ProtocolHandler<T>) handlerClass.newInstance();
            return handler;
        }
        return null;
    }
}

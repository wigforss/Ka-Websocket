package org.kasource.web.websocket.config;


import java.util.Map;

import org.kasource.web.websocket.protocol.ProtocolHandler;

public class BinaryProtocolHandlerConfigImpl implements ProtocolHandlerConfig<byte[]>{

    private ProtocolHandler<byte[]> defaultProtocol;
   
    private  Map<String, ProtocolHandler<byte[]>> protocolHandlers;
      
    @Override
    public ProtocolHandler<byte[]> getDefaultProtocol() {
        return defaultProtocol;
    }


    /**
     * @param defaultHandler the defaultHandler to set
     */
    public void setDefaultProtocol(ProtocolHandler<byte[]> defaultProtocol) {
        this.defaultProtocol = defaultProtocol;
    }

    /**
     * @param handlers the handlers to set
     */
    public void setProtocolHandlers(Map<String, ProtocolHandler<byte[]>> protocolHandlers) {
        this.protocolHandlers = protocolHandlers;
    }

    @Override
    public Map<String, ProtocolHandler<byte[]>> getProtocolHandlers() {
        return protocolHandlers;
    }

    

}

package org.kasource.web.webocket.dropwizard.util;

import org.kasource.web.webocket.dropwizard.config.WebSocketConfiguration;
import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.loader.AbstractClientConfigBuilder;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;

/**
 * Load root configuration from the YAML configuration Object.
 * 
 * @author rikardwi
 */
public class ConfigLoader extends AbstractClientConfigBuilder {
    
    public WebSocketConfigImpl configure(WebSocketConfiguration dwConfig) {
        WebSocketConfigImpl config = new WebSocketConfigImpl();
        TextProtocolHandlerConfigImpl textProtocols = null;
        BinaryProtocolHandlerConfigImpl binaryProtocols = null;
       
        if (dwConfig.getOriginWhitelist() != null) {
            config.setOriginWhitelist(dwConfig.getOriginWhitelist());
        }
        if (dwConfig.getTextProtocols() != null) {
            textProtocols = super.getTextProtocols(dwConfig.getTextProtocols().getDefaultProtocol(), 
                                   dwConfig.getTextProtocols().getProtocols());
        }
        if (dwConfig.getBinaryProtocols() != null) {
            binaryProtocols = super.getBinaryProtocols(dwConfig.getBinaryProtocols().getDefaultProtocol(), 
                                                       dwConfig.getBinaryProtocols().getProtocols());
        }
        config.setClientIdGenerator(super.getClientIdGenerator(dwConfig.getClientIdGenerator()));
        config.setProtocolRepository(new ProtocolRepositoryImpl(textProtocols, binaryProtocols));
        return config;
    }

}

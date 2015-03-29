package org.kasource.web.webocket.dropwizard.config;

import java.util.Map;

import org.kasource.web.websocket.protocol.BinaryProtocolHandler;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Binary Protocol Handlers.
 * 
 * @author rikardwi
 */
public class BinaryProtocols {
    
    /**
     * The default protocol handler to be applied on all requests unless a specific protocol is 
     * requested. If the this is not set messages are simply sent as byte[].
     */
    @JsonProperty
    private Class<? extends BinaryProtocolHandler> defaultProtocol;
    
    /**
     * Available sub protocols that can be requested by the client, typically mapped by MIME type.
     */
    @JsonProperty
    private Map<String, Class<? extends BinaryProtocolHandler>> protocols;

    /**
     * @return the defaultProtocol
     */
    public Class<? extends BinaryProtocolHandler> getDefaultProtocol() {
        return defaultProtocol;
    }

    /**
     * @return the protocols
     */
    public Map<String, Class<? extends BinaryProtocolHandler>> getProtocols() {
        return protocols;
    }
}

package org.kasource.web.webocket.dropwizard.config;

import java.util.Map;

import org.kasource.web.websocket.protocol.TextProtocolHandler;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Text Protocol Handlers.
 * 
 * @author rikardwi
 */
public class TextProtocols {
    
   /**
    * The default protocol handler to be applied on all requests unless a specific protocol is 
    * requested. If the this is not set, messages are simply sent as String.
    */
    @JsonProperty
    private Class<? extends TextProtocolHandler> defaultProtocol;
   
    /**
     * Available sub protocols that can be requested by the client, typically mapped by MIME type.
     */
    @JsonProperty
    private Map<String, Class<? extends TextProtocolHandler>> protocols;

    /**
     * @return the defaultProtocol
     */
    public Class<? extends TextProtocolHandler> getDefaultProtocol() {
        return defaultProtocol;
    }

    /**
     * @return the protocols
     */
    public Map<String, Class<? extends TextProtocolHandler>> getProtocols() {
        return protocols;
    }
}

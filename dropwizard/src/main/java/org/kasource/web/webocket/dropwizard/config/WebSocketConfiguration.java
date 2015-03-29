package org.kasource.web.webocket.dropwizard.config;

import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Root configuration for Websockets.
 * 
 * @author rikardwi
 */
public class WebSocketConfiguration {
   
    /**
     * The client ID generator to override the default with.  
     **/
    @JsonProperty
    private Class<? extends ClientIdGenerator> clientIdGenerator;
    
   /**
    * Web Origins to allow, see https://tools.ietf.org/html/rfc6454. Adding this element will
    * enforce origin check for all connections, but can be overridden by each web socket end
    * point individually.
    */
    @JsonProperty
    private Set<String> originWhitelist;
   
    /**
     * Binary protocol handlers (implementing org.kasource.web.websocket.protocol.BinaryProtocolHandler) to be used.
     */
    @JsonProperty
    private BinaryProtocols binaryProtocols;
    
    /**
     * Text protocol handlers (implementing org.kasource.web.websocket.protocol.TextProtocolHandler) to be used.
     */
    @JsonProperty
    private TextProtocols textProtocols;
    

  

    /**
     * @return the originWhitelist
     */
    public Set<String> getOriginWhitelist() {
        return originWhitelist;
    }

    /**
     * @return the binaryProtocols
     */
    public BinaryProtocols getBinaryProtocols() {
        return binaryProtocols;
    }

    /**
     * @return the textProtocols
     */
    public TextProtocols getTextProtocols() {
        return textProtocols;
    }

    /**
     * @return the clientIdGenerator
     */
    public Class<? extends ClientIdGenerator> getClientIdGenerator() {
        return clientIdGenerator;
    }
}

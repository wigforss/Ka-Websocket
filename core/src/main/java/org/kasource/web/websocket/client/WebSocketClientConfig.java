package org.kasource.web.websocket.client;

import java.util.Map;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.util.HeaderLookup;

public class WebSocketClientConfig {
    private final WebSocketManager manager; 
    private final String url;
    private final String username; 
    private final String clientId; 
    private final Map<String, String[]> connectionParameters; 
    private final String subProtocol;
    
    private WebSocketClientConfig(Builder builder) {
        this.manager = builder.manager;
        this.url = builder.url;
        this.username = builder.username;
        this.clientId = builder.clientId;
        this.connectionParameters = builder.connectionParameters;
        this.subProtocol = builder.subProtocol;
    }
    
    public static class Builder {
        private WebSocketManager manager; 
        private String url;
        private String username; 
        private String clientId; 
        private Map<String, String[]> connectionParameters; 
        private String subProtocol;
        
        Builder(WebSocketManager manager, String clientId, Map<String, String[]> connectionParameters) {
            this.manager = manager;
            this.clientId = clientId;
            this.connectionParameters = connectionParameters;
        }
        
        public Builder url(String urlToUse) {
            this.url = urlToUse;
            return this;
        }
        
        public Builder username(String authenticatedUser) {
            this.username = authenticatedUser;
            return this;
        }
        
        
        public Builder subProtocol(String protocolName) {
            this.subProtocol = protocolName;
            return this;
        }
        
        public WebSocketClientConfig build() {
            if(url == null) {
                throw new IllegalStateException("url is needed to create a WebSocketClient");
            }
            
            return new WebSocketClientConfig(this);
        }

       
    }

    /**
     * @return the manager
     */
    public WebSocketManager getManager() {
        return manager;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return the connectionParameters
     */
    public Map<String, String[]> getConnectionParameters() {
        return connectionParameters;
    }

    /**
     * @return the subProtocol
     */
    public String getSubProtocol() {
        return subProtocol;
    }
}

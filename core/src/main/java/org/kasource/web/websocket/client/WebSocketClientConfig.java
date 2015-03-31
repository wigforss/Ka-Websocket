package org.kasource.web.websocket.client;


import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolRepository;

public class WebSocketClientConfig {
    private final ClientChannel clientChannel; 
    private final String url;
    private final String username; 
    private final String clientId; 
    private final UpgradeRequestData request; 
    private final String subProtocol;
    private final ProtocolHandler<String> textProtocolHandler;
    private final ProtocolHandler<byte[]> binaryProtocolHandler;
    
    private WebSocketClientConfig(Builder builder) {
        this.clientChannel = builder.clientChannel;
        this.url = builder.url;
        this.username = builder.username;
        this.clientId = builder.clientId;
        this.request = builder.request;
        this.subProtocol = builder.subProtocol;
        this.binaryProtocolHandler = builder.binaryProtocolHandler;
        this.textProtocolHandler = builder.textProtocolHandler;
    }
    
    public static class Builder {
        private ClientChannel clientChannel; 
        private String url;
        private String username; 
        private String clientId; 
        private UpgradeRequestData request; 
        private String subProtocol;
        private ProtocolHandler<String> textProtocolHandler;
        private ProtocolHandler<byte[]> binaryProtocolHandler;
        
        Builder(ClientChannel clientChannel, String clientId, UpgradeRequestData request) {
            this.clientChannel = clientChannel;
            this.clientId = clientId;
            this.request = request;
        }
        
        public WebSocketClientConfig build() {
            if(url == null) {
                throw new IllegalStateException("url is needed to create a WebSocketClient");
            }
            
            return new WebSocketClientConfig(this);
        }
        
        public Builder url(String urlToUse) {
            this.url = urlToUse;
            return this;
        }
        
        public Builder username(String authenticatedUser) {
            this.username = authenticatedUser;
            return this;
        }
        
        
        public Builder protocol(String subProtocol, ProtocolRepository protocolRepository) {
            this.subProtocol = subProtocol;
            textProtocolHandler = protocolRepository.getDefaultTextProtocol();
            binaryProtocolHandler = protocolRepository.getDefaultBinaryProtocol();
           
            if (subProtocol != null && !subProtocol.isEmpty()) {
                textProtocolHandler = protocolRepository.getTextProtocol(subProtocol, true);
                binaryProtocolHandler = protocolRepository.getBinaryProtocol(subProtocol, true);
            }
            return this;
        }
        
       

        /**
         * @return the textProtocolHandler
         */
        public ProtocolHandler<String> getTextProtocolHandler() {
            return textProtocolHandler;
        }

        /**
         * @param textProtocolHandler the textProtocolHandler to set
         */
        public void setTextProtocolHandler(ProtocolHandler<String> textProtocolHandler) {
            this.textProtocolHandler = textProtocolHandler;
        }

       
    }

    /**
     * @return the manager
     */
    public ClientChannel getManager() {
        return clientChannel;
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
    public UpgradeRequestData getRequest() {
        return request;
    }

    /**
     * @return the subProtocol
     */
    public String getSubProtocol() {
        return subProtocol;
    }

    /**
     * @return the textProtocolHandler
     */
    public ProtocolHandler<String> getTextProtocolHandler() {
        return textProtocolHandler;
    }

    /**
     * @return the binaryProtocolHandler
     */
    public ProtocolHandler<byte[]> getBinaryProtocolHandler() {
        return binaryProtocolHandler;
    }
}

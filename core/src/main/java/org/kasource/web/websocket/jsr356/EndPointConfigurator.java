package org.kasource.web.websocket.jsr356;

import java.util.List;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.EndpointConfig;
import org.kasource.web.websocket.security.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndPointConfigurator extends ServerEndpointConfig.Configurator {
    private static final Logger LOG = LoggerFactory.getLogger(EndPointConfigurator.class);
    
   
    private String selectedProtocol;
  
    private EndpointConfig endpointConfig;
    private WebSocketClientConfig client;
    
    public EndPointConfigurator(EndpointConfig endpointConfig) { 
       this.endpointConfig = endpointConfig;
    }
    
    @Override
    public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
        selectedProtocol =  super.getNegotiatedSubprotocol(supported, requested);
        return selectedProtocol;
    } 
    
    @Override
    public boolean checkOrigin(String originHeaderValue) {
         if (endpointConfig.getOriginWhitelist() != null) {
             return endpointConfig.getOriginWhitelist().contains(originHeaderValue);
         } else {
             return true;
         }
    }
    
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        
        
        String url = endpointConfig.getUrl();
        if (endpointConfig.isDynamicAddressing()) {
           // url = request.getRequestURI().substring(request.getContextPath().length());
        }
        ClientChannel clientChannel = endpointConfig.getClientChannelFor(url);
        
        String username = null;
        try {
            username = clientChannel.authenticate(endpointConfig.getAuthenticationProvider(), request);
        } catch (AuthenticationException e) {
           LOG.warn("Unauthorized access for " + request, e);  
           throw new SecurityException("Unauthorized access for " + request);
        }
       
        client = endpointConfig.getClientBuilder(clientChannel).get(request)
                                            .username(username)
                                            .url(url)
                                            .protocol(selectedProtocol, endpointConfig.getProtocolRepository())
                                            .build();
        
        
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
       return (T) new WebSocketClientJsr356(client);
    }
    
 
    
}

package org.kasource.web.websocket.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.security.AuthenticationProvider;

public class WebSocketConfigImpl implements WebSocketConfig {
   
    
    private Set<String> originWhitelist = new HashSet<String>();  
    private ProtocolRepository protocolRepository;   
    private ClientChannelRepository clientChannelRepository;  
    private ServerChannelFactory serverChannelFactory;
    private Map<String, ClientConfig> clientConfigs = new HashMap<String, ClientConfig>();
    private WebSocketListenerRegister listenerRegister; 
    private ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();  
    private AuthenticationProvider authenticationProvider;
    
    public void registerClientConfig(ClientConfigImpl servletConfig) {
       //Override empty or default values
        
        if (servletConfig.getAuthenticationProvider() == null) {
            servletConfig.setAuthenticationProvider(authenticationProvider);
        }
        if (servletConfig.getClientIdGenerator() == null || servletConfig.getClientIdGenerator() instanceof DefaultClientIdGenerator) {
            servletConfig.setClientIdGenerator(clientIdGenerator);
        }
        if (servletConfig.getProtocolRepository().isEmpty() && protocolRepository != null) {
            servletConfig.setProtocolRepository(protocolRepository);
        }
        if (servletConfig.getOriginWhitelist() == null) {
            servletConfig.setOriginWhitelist(originWhitelist);
        }
        servletConfig.setManagerRepository(clientChannelRepository);
        
        getClientConfigs().put(servletConfig.getServletName(), servletConfig);
    }

    /**
     * @param clientIdGenerator the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        if (clientIdGenerator != null) {
            this.clientIdGenerator = clientIdGenerator;
        }
    }
    
    
    /**
     * @param orginWhitelist the orginWhitelist to set
     */
    public void setOriginWhitelist(Set<String> orginWhitelist) {
        this.originWhitelist = orginWhitelist;
    }


    /**
     * @param protocolRepository the protocolHandlerRepository to set
     */
    public void setProtocolRepository(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

 
    /**
     * @return the channelFactory
     */
    @Override
    public ServerChannelFactory getServerChannelFactory() {
        return serverChannelFactory;
    }

    /**
     * @param channelFactory the channelFactory to set
     */
    public void setServerChannelFactory(ServerChannelFactory serverChannelFactory) {
        this.serverChannelFactory = serverChannelFactory;
    }

    /**
     * @return the clientChannelRepository
     */
    @Override
    public ClientChannelRepository getClientChannelRepository() {
        return clientChannelRepository;
    }

    /**
     * @param clientClientChannelRepository the clientChannelRepository to set
     */
    public void setManagerRepository(ClientChannelRepository clientChannelRepository) {
        this.clientChannelRepository = clientChannelRepository;
    }

    /**
     * @return the servletConfig
     */
    @Override
    public ClientConfig getClientConfig(String servletName) {
        return clientConfigs.get(servletName);
    }

    /**
     * @return the servletConfigs
     */
    public Map<String, ClientConfig> getClientConfigs() {
        return clientConfigs;
    }

    /**
     * @param servletConfigs the servletConfigs to set
     */
    public void setClientConfigs(Map<String, ClientConfig> clientConfigs) {
        this.clientConfigs = clientConfigs;
    }

    /**
     * @return the listenerRegister
     */
    @Override
    public WebSocketListenerRegister getListenerRegister() {
        return listenerRegister;
    }

    /**
     * @param listenerRegister the listenerRegister to set
     */
    public void setListenerRegister(WebSocketListenerRegister listenerRegister) {
        this.listenerRegister = listenerRegister;
    }


    /**
     * @param authenticationProvider the authenticationProvider to set
     */
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

}

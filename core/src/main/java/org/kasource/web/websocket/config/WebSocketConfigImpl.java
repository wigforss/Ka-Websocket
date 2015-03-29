package org.kasource.web.websocket.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;

public class WebSocketConfigImpl implements WebSocketConfig {
   
    
    private Set<String> originWhitelist = new HashSet<String>();  
    private ProtocolRepository protocolRepository;   
    private WebSocketManagerRepository managerRepository;  
    private WebSocketChannelFactory channelFactory;
    private Map<String, WebSocketServletConfig> servletConfigs = new HashMap<String, WebSocketServletConfig>();
    private WebSocketListenerRegister listenerRegister; 
    private ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();  
    private AuthenticationProvider authenticationProvider;
    
    public void registerServlet(WebSocketServletConfigImpl servletConfig) {
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
        servletConfig.setManagerRepository(managerRepository);
        
        getServletConfigs().put(servletConfig.getServletName(), servletConfig);
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
    public WebSocketChannelFactory getChannelFactory() {
        return channelFactory;
    }

    /**
     * @param channelFactory the channelFactory to set
     */
    public void setChannelFactory(WebSocketChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    /**
     * @return the managerRepository
     */
    @Override
    public WebSocketManagerRepository getManagerRepository() {
        return managerRepository;
    }

    /**
     * @param managerRepository the managerRepository to set
     */
    public void setManagerRepository(WebSocketManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * @return the servletConfig
     */
    @Override
    public WebSocketServletConfig getServletConfig(String servletName) {
        return servletConfigs.get(servletName);
    }

    /**
     * @return the servletConfigs
     */
    public Map<String, WebSocketServletConfig> getServletConfigs() {
        return servletConfigs;
    }

    /**
     * @param servletConfigs the servletConfigs to set
     */
    public void setServletConfigs(Map<String, WebSocketServletConfig> servletConfigs) {
        this.servletConfigs = servletConfigs;
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

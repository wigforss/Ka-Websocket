package org.kasource.web.websocket.config;

import java.util.HashSet;
import java.util.Set;

import org.kasource.web.websocket.client.WebSocketClientBuilderFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.security.AuthenticationProvider;

public class WebSocketServletConfigImpl implements WebSocketServletConfig {
    private String servletName;
    private boolean dynamicAddressing;
    private ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();
    private WebSocketManagerRepository managerRepository;
    private ProtocolRepository protocolRepository = new ProtocolRepositoryImpl();
    private Set<String> originWhitelist = new HashSet<String>();
    private AuthenticationProvider authenticationProvider;
    
    @Override
    public boolean isDynamicAddressing() {
        return dynamicAddressing;
    }

    @Override
    public WebSocketClientBuilderFactory getClientBuilder(WebSocketManager manager) {
        return new WebSocketClientBuilderFactory(manager, clientIdGenerator);
    }

    @Override
    public WebSocketManager getWebSocketManager(String url) {
        return managerRepository.getWebSocketManager(url);
    }

   

    @Override
    public boolean isValidOrigin(String origin) {
        if (origin == null) {
            return false;
        }
        if (originWhitelist != null && !originWhitelist.isEmpty()) {
            return originWhitelist.contains(origin);
        }
        return true;
    }

    /**
     * @return the servletName
     */
    @Override
    public String getServletName() {
        return servletName;
    }

    /**
     * @param servletName
     *            the servletName to set
     */
    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    /**
     * @return the managerRepository
     */
    public WebSocketManagerRepository getManagerRepository() {
        return managerRepository;
    }

    /**
     * @param managerRepository
     *            the managerRepository to set
     */
    public void setManagerRepository(WebSocketManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * @return the protocolRepository
     */
    public ProtocolRepository getProtocolRepository() {
        return protocolRepository;
    }

    /**
     * @param protocolRepository
     *            the protocolRepository to set
     */
    public void setProtocolRepository(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    /**
     * @return the originWhitelist
     */
    @Override
    public Set<String> getOriginWhitelist() {
        return originWhitelist;
    }

    /**
     * @param originWhitelist
     *            the originWhitelist to set
     */
    public void setOriginWhitelist(Set<String> originWhitelist) {
        this.originWhitelist = originWhitelist;
    }

    /**
     * @param dynamicAddressing
     *            the dynamicAddressing to set
     */
    public void setDynamicAddressing(boolean dynamicAddressing) {
        this.dynamicAddressing = dynamicAddressing;
    }

    /**
     * @param clientIdGenerator
     *            the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }


    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    /**
     * @param authenticationProvider the authenticationProvider to set
     */
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public void merge(WebSocketServletConfig config) {
        if (authenticationProvider == null) {
            authenticationProvider = config.getAuthenticationProvider();
        }
        if (originWhitelist == null || originWhitelist.isEmpty()) {
            originWhitelist = config.getOriginWhitelist();
        }
        if (clientIdGenerator instanceof DefaultClientIdGenerator) {
            if (config.getClientIdGenerator() != null) {
                clientIdGenerator = config.getClientIdGenerator();
            }
        }
        
        if (protocolRepository == null) {
            protocolRepository = config.getProtocolRepository();
        }
        
        
    }

    /**
     * @return the clientIdGenerator
     */
    @Override
    public ClientIdGenerator getClientIdGenerator() {
        return clientIdGenerator;
    }



}

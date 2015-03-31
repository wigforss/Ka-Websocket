package org.kasource.web.websocket.config;

import java.util.HashSet;
import java.util.Set;

import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.client.WebSocketClientBuilderFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.security.AuthenticationProvider;

public class ClientConfigImpl implements ClientConfig {
    private String servletName;
    private boolean dynamicAddressing;
    private ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();
    private ClientChannelRepository managerRepository;
    private ProtocolRepository protocolRepository = new ProtocolRepositoryImpl();
    private Set<String> originWhitelist = new HashSet<String>();
    private AuthenticationProvider authenticationProvider;
    
    @Override
    public boolean isDynamicAddressing() {
        return dynamicAddressing;
    }

    @Override
    public WebSocketClientBuilderFactory getClientBuilder(ClientChannel manager) {
        return new WebSocketClientBuilderFactory(manager, clientIdGenerator);
    }

    @Override
    public ClientChannel getClientChannelFor(String url) {
        return managerRepository.getClientChannel(url);
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
    public ClientChannelRepository getManagerRepository() {
        return managerRepository;
    }

    /**
     * @param managerRepository
     *            the managerRepository to set
     */
    public void setManagerRepository(ClientChannelRepository managerRepository) {
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

    
    /**
     * @return the clientIdGenerator
     */
    @Override
    public ClientIdGenerator getClientIdGenerator() {
        return clientIdGenerator;
    }



}

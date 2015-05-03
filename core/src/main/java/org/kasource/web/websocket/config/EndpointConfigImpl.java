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

public class EndpointConfigImpl implements EndpointConfig {
    private String name;
    private boolean dynamicAddressing;
    private ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();
    private ClientChannelRepository managerRepository;
    private ProtocolRepository protocolRepository = new ProtocolRepositoryImpl();
    private Set<String> originWhitelist = new HashSet<String>();
    private AuthenticationProvider authenticationProvider;
    private String url;
    private long asyncSendTimeoutMillis = -1;
    private int maxBinaryMessageBufferSizeByte = -1;
    private long maxSessionIdleTimeoutMillis = -1;
    private int maxTextMessageBufferSizeByte = 1;
    
    @Override
    public boolean isDynamicAddressing() {
        return dynamicAddressing;
    }

    @Override
    public WebSocketClientBuilderFactory getClientBuilder(ClientChannel manager) {
        return new WebSocketClientBuilderFactory(this, manager, clientIdGenerator);
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
    public String getName() {
        return name;
    }

    /**
     * @param servletName
     *            the servletName to set
     */
    public void setName(String servletName) {
        this.name = servletName;
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

    @Override
    public String getUrl() {       
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param asyncSendTimeoutMillis the asyncSendTimeoutMillis to set
     */
    public void setAsyncSendTimeoutMillis(long asyncSendTimeoutMillis) {
        this.asyncSendTimeoutMillis = asyncSendTimeoutMillis;
    }

    /**
     * @param maxBinaryMessageBufferSizeByte the maxBinaryMessageBufferSizeByte to set
     */
    public void setMaxBinaryMessageBufferSizeByte(int maxBinaryMessageBufferSizeByte) {
        this.maxBinaryMessageBufferSizeByte = maxBinaryMessageBufferSizeByte;
    }

    /**
     * @param maxSessionIdleTimeoutMillis the maxSessionIdleTimeoutMillis to set
     */
    public void setMaxSessionIdleTimeoutMillis(long maxSessionIdleTimeoutMillis) {
        this.maxSessionIdleTimeoutMillis = maxSessionIdleTimeoutMillis;
    }

    /**
     * @param maxTextMessageBufferSizeByte the maxTextMessageBufferSizeByte to set
     */
    public void setMaxTextMessageBufferSizeByte(int maxTextMessageBufferSizeByte) {
        this.maxTextMessageBufferSizeByte = maxTextMessageBufferSizeByte;
    }

    /**
     * @return the asyncSendTimeoutMillis
     */
    @Override
    public long getAsyncSendTimeoutMillis() {
        return asyncSendTimeoutMillis;
    }

    /**
     * @return the maxBinaryMessageBufferSizeByte
     */
    @Override
    public int getMaxBinaryMessageBufferSizeByte() {
        return maxBinaryMessageBufferSizeByte;
    }

    /**
     * @return the maxSessionIdleTimeoutMillis
     */
    @Override
    public long getMaxSessionIdleTimeoutMillis() {
        return maxSessionIdleTimeoutMillis;
    }

    /**
     * @return the maxTextMessageBufferSizeByte
     */
    @Override
    public int getMaxTextMessageBufferSizeByte() {
        return maxTextMessageBufferSizeByte;
    }



}

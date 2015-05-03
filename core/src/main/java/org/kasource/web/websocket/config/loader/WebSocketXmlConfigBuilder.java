package org.kasource.web.websocket.config.loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.kasource.commons.reflection.util.ClassUtils;
import org.kasource.web.websocket.channel.client.ClientChannelRepositoryImpl;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.client.id.AbstractClientIdGenerator;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.EndpointConfigImpl;
import org.kasource.web.websocket.config.xml.AuthenticationProviderXmlConfig;
import org.kasource.web.websocket.config.xml.OriginWhitelistXmlConfig;
import org.kasource.web.websocket.config.xml.ProtocolHandlerXmlConfig;
import org.kasource.web.websocket.config.xml.ProtocolXmlConfig;
import org.kasource.web.websocket.config.xml.WebsocketConfigXmlConfig;
import org.kasource.web.websocket.config.xml.WebsocketXmlConfig;
import org.kasource.web.websocket.protocol.BinaryProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;
import org.kasource.web.websocket.security.AbstractAuthenticationProvider;
import org.kasource.web.websocket.security.AuthenticationProvider;

public class WebSocketXmlConfigBuilder extends AbstractEndpointConfigBuilder {
    public WebSocketConfigImpl configure(WebsocketConfigXmlConfig xmlConfig, ServletContext servletContext) {
        WebSocketConfigImpl config = new WebSocketConfigImpl();
        config.setServerChannelFactory((ServerChannelFactory) servletContext.getAttribute(ServerChannelFactory.class.getName()));
        config.setManagerRepository(new ClientChannelRepositoryImpl(servletContext));
        config.setListenerRegister(new WebSocketListenerRegisterImpl(servletContext));
        TextProtocolHandlerConfigImpl textProtocols = getTextProtocolsFromXml(xmlConfig.getTextProtocolHandler());
        BinaryProtocolHandlerConfigImpl binaryProtocols = getBinaryProtocolsFromXml(xmlConfig.getBinaryProtocolHandler());
        ProtocolRepositoryImpl protocolRepository = new ProtocolRepositoryImpl(textProtocols, binaryProtocols);
        config.setClientIdGenerator(getClientIdGeneratorFromXml(xmlConfig.getClientIdGenerator()));
       

        config.setAuthenticationProvider(getAuthenticationProviderFromXml(xmlConfig.getAuthenticationProvider()));
        config.setOriginWhitelist(getOriginWhitelistFromXml(xmlConfig.getOriginWhitelist()));
        config.setProtocolRepository(protocolRepository);
        
        for (WebsocketXmlConfig websocketXmlConfig: xmlConfig.getWebsocket()) {
            config.registerEndpointConfig(loadServletConfig(websocketXmlConfig));
        }
        
        return config;
    }
    
    private EndpointConfigImpl loadServletConfig(WebsocketXmlConfig websocketXmlConfig) {
        EndpointConfigImpl config = new EndpointConfigImpl();
        config.setName(websocketXmlConfig.getServletName());
        config.setClientIdGenerator(getClientIdGeneratorFromXml(websocketXmlConfig.getClientIdGenerator()));
        config.setAuthenticationProvider(getAuthenticationProviderFromXml(websocketXmlConfig.getAuthenticationProvider()));
        config.setDynamicAddressing(websocketXmlConfig.isDynamicAddressing());
        TextProtocolHandlerConfigImpl textProtocols = getTextProtocolsFromXml(websocketXmlConfig.getTextProtocolHandler());
        BinaryProtocolHandlerConfigImpl binaryProtocols = getBinaryProtocolsFromXml(websocketXmlConfig.getBinaryProtocolHandler());
        ProtocolRepositoryImpl protocolRepository = new ProtocolRepositoryImpl(textProtocols, binaryProtocols);
        config.setProtocolRepository(protocolRepository);
        config.setOriginWhitelist(getOriginWhitelistFromXml(websocketXmlConfig.getOriginWhitelist()));
        
        return config;
    }

    private Set<String> getOriginWhitelistFromXml(OriginWhitelistXmlConfig originWhitelistXmlConfig) {
        if (originWhitelistXmlConfig != null) {
            Set<String> allowedOrigins = new HashSet<String>();
            allowedOrigins.addAll(originWhitelistXmlConfig.getOrigin());
            return allowedOrigins;
        }
        return null;
    }
    
    private ClientIdGenerator getClientIdGeneratorFromXml(org.kasource.web.websocket.config.xml.ClientIdGeneratorXmlConfig xmlConfig) {
        boolean headerBased = false;
        String usernameKey = null;
        Class<? extends ClientIdGenerator> clientIdGeneratorClass = null;
        if (xmlConfig != null) {
            String clientIdGeneratorClassName = xmlConfig.getClazz();
            if (clientIdGeneratorClassName != null && !clientIdGeneratorClassName.trim().isEmpty()) {
                clientIdGeneratorClass = ClassUtils.loadClass(clientIdGeneratorClassName, ClientIdGenerator.class);
            }
            headerBased = xmlConfig.isHeaderValue();
            usernameKey = xmlConfig.getIdKey();
        }
        ClientIdGenerator idGenerator = super.getClientIdGenerator(clientIdGeneratorClass);
        
        if (idGenerator != null && idGenerator instanceof AbstractClientIdGenerator) {
            AbstractClientIdGenerator idGeneratorBase = (AbstractClientIdGenerator) idGenerator;
            idGeneratorBase.setUseHeaderValue(headerBased);
            if (usernameKey != null) {
                idGeneratorBase.setIdKey(usernameKey);
               
            }
        }
        
        return idGenerator;
        
    }
   
    private TextProtocolHandlerConfigImpl getTextProtocolsFromXml(ProtocolHandlerXmlConfig protocolHandlerXmlConfig) {
        if (protocolHandlerXmlConfig == null) {
            return null;
        }
        Class<? extends TextProtocolHandler> defaultTextProtocolClass = null;
        String defaultTextProtocolClassName = protocolHandlerXmlConfig.getDefaultHandlerClass();
        if (defaultTextProtocolClassName != null && !defaultTextProtocolClassName.trim().isEmpty()) {
            defaultTextProtocolClass = ClassUtils.loadClass(defaultTextProtocolClassName.trim(), TextProtocolHandler.class);
        }
        Map<String, Class<? extends TextProtocolHandler>> textProtocolMap = new HashMap<String, Class<? extends TextProtocolHandler>>();
        for (ProtocolXmlConfig protocolXmlConfig : protocolHandlerXmlConfig.getProtocol()) {
            String textProtocolClassName = protocolXmlConfig.getHandlerClass();
            if (textProtocolClassName != null && !textProtocolClassName.trim().isEmpty()) {
              
                textProtocolMap.put(protocolXmlConfig.getProtocol(), ClassUtils.loadClass(textProtocolClassName.trim(), TextProtocolHandler.class));
            }
        }
        return super.getTextProtocols(defaultTextProtocolClass, textProtocolMap);
    }
    
    private BinaryProtocolHandlerConfigImpl getBinaryProtocolsFromXml(ProtocolHandlerXmlConfig protocolHandlerXmlConfig) {
        if (protocolHandlerXmlConfig == null) {
            return null;
        }
        Class<? extends BinaryProtocolHandler> defaultBinaryProtocolClass = null;
        String defaultBinaryProtocolClassName = protocolHandlerXmlConfig.getDefaultHandlerClass();
        if (defaultBinaryProtocolClassName != null && !defaultBinaryProtocolClassName.trim().isEmpty()) {
            defaultBinaryProtocolClass = ClassUtils.loadClass(defaultBinaryProtocolClassName.trim(), BinaryProtocolHandler.class);
        }
        Map<String, Class<? extends BinaryProtocolHandler>> binaryProtocolMap = new HashMap<String, Class<? extends BinaryProtocolHandler>>();
        for (ProtocolXmlConfig protocolXmlConfig : protocolHandlerXmlConfig.getProtocol()) {
            String binaryProtocolClassName = protocolXmlConfig.getHandlerClass();
            if (binaryProtocolClassName != null && !binaryProtocolClassName.trim().isEmpty()) {
              
                binaryProtocolMap.put(protocolXmlConfig.getProtocol(), ClassUtils.loadClass(binaryProtocolClassName.trim(), BinaryProtocolHandler.class));
            }
        }
        return super.getBinaryProtocols(defaultBinaryProtocolClass, binaryProtocolMap);
    }
 
    private AuthenticationProvider getAuthenticationProviderFromXml(AuthenticationProviderXmlConfig xmlConfig) {
        Class<? extends AuthenticationProvider> authenticationProviderClass = null;
        boolean headerBased = false;
        String usernameKey = null;
        String passwordKey = null;
        if (xmlConfig != null) {
            headerBased = xmlConfig.isHeaderAuthentication();
            usernameKey = xmlConfig.getUsernameKey();
            passwordKey = xmlConfig.getPasswordKey();
            String authenticationProviderClassName = xmlConfig.getProvider();
            if (authenticationProviderClassName != null && !authenticationProviderClassName.trim().isEmpty()) {
                authenticationProviderClass = ClassUtils.loadClass(authenticationProviderClassName.trim(), AuthenticationProvider.class);
            }
        }
        
        AuthenticationProvider authProvider = super.getAuthenticationProvider(authenticationProviderClass);
        
        if (authProvider != null && authProvider instanceof AbstractAuthenticationProvider) {
            AbstractAuthenticationProvider authenticationProvider = (AbstractAuthenticationProvider) authProvider;
            authenticationProvider.setHeaderBased(headerBased);
            if (usernameKey != null) {
                authenticationProvider.setUsernameKey(usernameKey);
            }
            if (passwordKey != null) {
                authenticationProvider.setPasswordKey(passwordKey);
            }
        }
        
        return authProvider;
    }
}

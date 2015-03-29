package org.kasource.web.websocket.config.loader;

import java.util.HashMap;
import java.util.Map;

import org.kasource.web.websocket.client.id.AbstractClientIdGenerator;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.protocol.BinaryProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.security.AbstractAuthenticationProvider;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;

public abstract class AbstractwebSocketServletConfigBuilder {
    
    protected TextProtocolHandlerConfigImpl getTextProtocols(Class<? extends TextProtocolHandler> defaultTextProtocolClass,
                                                             Map<String, Class<? extends TextProtocolHandler>> textProtocols) {
        TextProtocolHandlerConfigImpl textProtocolConfig = new TextProtocolHandlerConfigImpl();
        
        ProtocolHandler<String> defaultTextProtocol = createTextProtocolHandler(defaultTextProtocolClass);
        
        if (defaultTextProtocol != null) {
                textProtocolConfig.setDefaultProtocol(defaultTextProtocol);
        }
        
        Map<String, ProtocolHandler<String>> textProtocolMap = new HashMap<String, ProtocolHandler<String>>();
        for (Map.Entry<String, Class<? extends TextProtocolHandler>> textProtocol : textProtocols.entrySet()) {
            ProtocolHandler<String> protocol = createTextProtocolHandler(textProtocol.getValue());
            if (protocol != null) {
                textProtocolMap.put(textProtocol.getKey(), protocol); 
            }
        }
        textProtocolConfig.setProtocolHandlers(textProtocolMap);
        return textProtocolConfig;
    }
    
    private ProtocolHandler<String> createTextProtocolHandler(Class<? extends TextProtocolHandler> textProtocol) {
        if (textProtocol != null) {
            try {
                return (ProtocolHandler<String>) textProtocol.newInstance();
            } catch (Exception e) {
                throw new WebSocketConfigException("Instance of class: " + textProtocol
                            + " could not be created (missing empty public constuctor?).", e);
            }
           
        }
        return null;
    }
        
    
    protected BinaryProtocolHandlerConfigImpl getBinaryProtocols(Class<? extends BinaryProtocolHandler> defaultBinaryProtocolClass,
                                                               Map<String, Class<? extends BinaryProtocolHandler>> binaryProtocols) {
        BinaryProtocolHandlerConfigImpl binaryProtocolConfig = new BinaryProtocolHandlerConfigImpl();
        
        ProtocolHandler<byte[]> defaultBinaryProtocol = createBinaryProtocolHandler(defaultBinaryProtocolClass);
        if (defaultBinaryProtocol != null) {
                binaryProtocolConfig.setDefaultProtocol(defaultBinaryProtocol);     
        }
        
        Map<String, ProtocolHandler<byte[]>> binaryProtocolMap = new HashMap<String, ProtocolHandler<byte[]>>();
           
        for (Map.Entry<String, Class<? extends BinaryProtocolHandler>> binaryProtocol : binaryProtocols.entrySet()) {
            ProtocolHandler<byte[]> protocol = createBinaryProtocolHandler(binaryProtocol.getValue());
            if (protocol != null) {
                binaryProtocolMap.put(binaryProtocol.getKey(), protocol); 
            }
        }
          
        binaryProtocolConfig.setProtocolHandlers(binaryProtocolMap);
        return binaryProtocolConfig;
    }
    
    private ProtocolHandler<byte[]> createBinaryProtocolHandler(Class<? extends BinaryProtocolHandler> binaryProtocol){
        if (binaryProtocol != null) {
            try {
                return (ProtocolHandler<byte[]>) binaryProtocol.newInstance();
            } catch (Exception e) {
                throw new WebSocketConfigException("Instance of class: " + binaryProtocol
                            + " could not be created (missing empty public constuctor?).", e);
            }
           
        }
        return null;
    }
    
    protected ClientIdGenerator getClientIdGenerator(Class<? extends ClientIdGenerator> idGeneratorClass) {
        ClientIdGenerator idGenerator = new DefaultClientIdGenerator();
        if (idGeneratorClass != null) {           
            try {
                return idGeneratorClass.newInstance();
            } catch (Exception e) {
                throw new WebSocketConfigException("Instance of class: " + idGeneratorClass
                            + " could not be created (missing empty public constuctor?).", e);
            }
        } 
        
        return idGenerator;
    }
    
    protected AuthenticationProvider getAuthenticationProvider(Class<? extends AuthenticationProvider> authenticationProviderClass) {
        AuthenticationProvider authProvider = null;
        if (authenticationProviderClass != null) {
            try {
                authProvider = (AuthenticationProvider) authenticationProviderClass.newInstance();
            } catch (Exception e) {
                throw new WebSocketConfigException("Instance of class: " + authenticationProviderClass
                            + " could not be created (missing empty public constuctor?).", e);
            }
        } 
       
        return authProvider;
    }
}

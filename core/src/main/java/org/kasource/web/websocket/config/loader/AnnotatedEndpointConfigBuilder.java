package org.kasource.web.websocket.config.loader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.EndpointConfigImpl;
import org.kasource.web.websocket.config.annotation.AllowedOrigin;
import org.kasource.web.websocket.config.annotation.Authenticate;
import org.kasource.web.websocket.config.annotation.BinaryProtocol;
import org.kasource.web.websocket.config.annotation.BinaryProtocols;
import org.kasource.web.websocket.config.annotation.DefaultBinaryProtocol;
import org.kasource.web.websocket.config.annotation.DefaultTextProtocol;
import org.kasource.web.websocket.config.annotation.GenerateId;
import org.kasource.web.websocket.config.annotation.TextProtocol;
import org.kasource.web.websocket.config.annotation.TextProtocols;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.protocol.BinaryProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.security.AuthenticationProvider;

public class AnnotatedEndpointConfigBuilder extends AbstractEndpointConfigBuilder implements EndpointAnnotationConfigurationBuilder {
    public EndpointConfigImpl configure(Class<?> webocketPojo) {
        EndpointConfigImpl config = new EndpointConfigImpl();

        WebSocket websocket = webocketPojo.getAnnotation(WebSocket.class);
        if (websocket == null) {
            throw new WebSocketConfigException(webocketPojo + " must be annotated with " + WebSocket.class);
        }

        config.setDynamicAddressing(websocket.dynamicAddressing());
        config.setUrl(websocket.value());
        config.setAsyncSendTimeoutMillis(websocket.asyncSendTimeoutMillis());
        config.setMaxBinaryMessageBufferSizeByte(websocket.maxBinaryMessageBufferSizeByte());
        config.setMaxSessionIdleTimeoutMillis(websocket.maxSessionIdleTimeoutMillis());
        config.setMaxTextMessageBufferSizeByte(websocket.maxTextMessageBufferSizeByte());
        config.setName(webocketPojo.getSimpleName());
        setAllowedOrigin(webocketPojo, config);
        config.setAuthenticationProvider(getAuthenticationProviderFromAnnotation(webocketPojo));
        setClientIdGenerator(webocketPojo, config);
        
        TextProtocolHandlerConfigImpl textProtocols = getTextProtocolsFromAnnotations(webocketPojo);
        BinaryProtocolHandlerConfigImpl binaryProtocols = getBinaryProtocolsFromAnnotations(webocketPojo);
        ProtocolRepositoryImpl protocolRepository = new ProtocolRepositoryImpl(textProtocols, binaryProtocols);
        config.setProtocolRepository(protocolRepository);
 
        return config;
    }
    
    private AuthenticationProvider getAuthenticationProviderFromAnnotation(Class<?> webocketPojo) {
        Class<? extends AuthenticationProvider> authenticationProviderClass = null;
       
        
        Authenticate auth = webocketPojo.getAnnotation(Authenticate.class);
        if (auth != null) {
            authenticationProviderClass = auth.value();
           
        }
        return super.getAuthenticationProvider(authenticationProviderClass);
    }

    private TextProtocolHandlerConfigImpl getTextProtocolsFromAnnotations(Class<?> webocketPojo) {
        Class<? extends TextProtocolHandler> defaultTextProtocolClass = null;
        DefaultTextProtocol defaultTextProtocol = webocketPojo.getAnnotation(DefaultTextProtocol.class);
        if (defaultTextProtocol != null) {
            defaultTextProtocolClass = defaultTextProtocol.value();
        }
        Map<String, Class<? extends TextProtocolHandler>> textProtocolMap = new HashMap<String, Class<? extends TextProtocolHandler>>();
        TextProtocols textProtocols = webocketPojo.getAnnotation(TextProtocols.class);
        if (textProtocols != null) {
            for (TextProtocol textProtocol : textProtocols.value()) {
                
                    textProtocolMap.put(textProtocol.protocol(), textProtocol.handler());
                
            }
        }
       
        return super.getTextProtocols(defaultTextProtocolClass, textProtocolMap);
    }
    
    private BinaryProtocolHandlerConfigImpl getBinaryProtocolsFromAnnotations(Class<?> webocketPojo) {
        Class<? extends BinaryProtocolHandler> defaultBinaryProtocolClass = null;
        DefaultBinaryProtocol defaultBinaryProtocol = webocketPojo.getAnnotation(DefaultBinaryProtocol.class);
        if (defaultBinaryProtocol != null) {
            defaultBinaryProtocolClass = defaultBinaryProtocol.value();
        }
        Map<String, Class<? extends BinaryProtocolHandler>> binaryProtocolMap = new HashMap<String, Class<? extends BinaryProtocolHandler>>();
        BinaryProtocols binaryProtocols = webocketPojo.getAnnotation(BinaryProtocols.class);
        if (binaryProtocols != null) {
            for (BinaryProtocol binaryProtocol : binaryProtocols.value()) {
                binaryProtocolMap.put(binaryProtocol.protocol(), binaryProtocol.handler());
            }
        }
        
        return super.getBinaryProtocols(defaultBinaryProtocolClass, binaryProtocolMap);
    }

    private void setAllowedOrigin(Class<?> webocketPojo, EndpointConfigImpl config) {
        AllowedOrigin allowedOrigin = webocketPojo.getAnnotation(AllowedOrigin.class);
        if (allowedOrigin != null) {
            Set<String> originWhitelist = new HashSet<String>();
            originWhitelist.addAll(Arrays.asList(allowedOrigin.value()));
            config.setOriginWhitelist(originWhitelist);
        }
    }

    private void setClientIdGenerator(Class<?> webocketPojo, EndpointConfigImpl config) {
      
        GenerateId generateId = webocketPojo.getAnnotation(GenerateId.class);
        Class<? extends ClientIdGenerator> idGeneratorClass = null;
        if (generateId != null) {
            idGeneratorClass = generateId.value();
        }
        config.setClientIdGenerator(super.getClientIdGenerator(idGeneratorClass));
    }
}

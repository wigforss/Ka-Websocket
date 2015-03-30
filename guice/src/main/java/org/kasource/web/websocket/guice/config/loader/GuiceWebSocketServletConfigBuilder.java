package org.kasource.web.websocket.guice.config.loader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
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
import org.kasource.web.websocket.config.loader.WebSocketServletAnnotationConfigurationBuilder;
import org.kasource.web.websocket.protocol.BinaryProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.security.AuthenticationProvider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

@Singleton
public class GuiceWebSocketServletConfigBuilder implements WebSocketServletAnnotationConfigurationBuilder {
    @Inject
    private Injector injector;
    
    public WebSocketServletConfigImpl configure(Class<?> webocketPojo) {
        WebSocketServletConfigImpl config = new WebSocketServletConfigImpl();

        WebSocket websocket = webocketPojo.getAnnotation(WebSocket.class);
        if (websocket == null) {
            throw new WebSocketConfigException(webocketPojo + " must be annotated with " + WebSocket.class);
        }

        config.setDynamicAddressing(websocket.dynamicAddressing());

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
       
        AuthenticationProvider provider = null;
        Authenticate auth = webocketPojo.getAnnotation(Authenticate.class);
        if (auth != null) {
            String beanName = auth.bean();
     
            if (beanName.isEmpty()) {
                Key<? extends AuthenticationProvider> bindKey = Key.get(auth.value());
                provider = injector.getInstance(bindKey);
            } else {
                Key<? extends AuthenticationProvider> bindKey = Key.get(auth.value(), Names.named(beanName));
                provider = injector.getInstance(bindKey);
            }
            return provider;
        }
        return null;
       
    }

    private TextProtocolHandlerConfigImpl getTextProtocolsFromAnnotations(Class<?> webocketPojo) {
        TextProtocolHandlerConfigImpl protocolHandlerConfigImpl = new TextProtocolHandlerConfigImpl();
        
        
        DefaultTextProtocol defaultTextProtocol = webocketPojo.getAnnotation(DefaultTextProtocol.class);
        if (defaultTextProtocol != null) {
            String beanName = defaultTextProtocol.bean();
            if (beanName.isEmpty()) {
                Key<? extends TextProtocolHandler> bindKey = Key.get(defaultTextProtocol.value());     
                protocolHandlerConfigImpl.setDefaultProtocol(injector.getInstance(bindKey));
            } else {
                Key<? extends TextProtocolHandler> bindKey = Key.get(defaultTextProtocol.value(), Names.named(beanName));
                protocolHandlerConfigImpl.setDefaultProtocol(injector.getInstance(bindKey));
            }
        }
        Map<String, ProtocolHandler<String>> textProtocolMap = new HashMap<String, ProtocolHandler<String>>();
        TextProtocols textProtocols = webocketPojo.getAnnotation(TextProtocols.class);
        if (textProtocols != null) {
            for (TextProtocol textProtocol : textProtocols.value()) {
                String beanName = textProtocol.bean();
                if (beanName.isEmpty()) {
                    Key<? extends TextProtocolHandler> bindKey = Key.get(textProtocol.handler()); 
                    textProtocolMap.put(textProtocol.protocol(), injector.getInstance(bindKey));
                } else {
                    Key<? extends TextProtocolHandler> bindKey = Key.get(textProtocol.handler(), Names.named(beanName));       
                    textProtocolMap.put(textProtocol.protocol(), injector.getInstance(bindKey));
                }
                    
                
            }
        }
        protocolHandlerConfigImpl.setProtocolHandlers(textProtocolMap);
        return protocolHandlerConfigImpl;
    }
    
    private BinaryProtocolHandlerConfigImpl getBinaryProtocolsFromAnnotations(Class<?> webocketPojo) {
        BinaryProtocolHandlerConfigImpl protocolHandlerConfigImpl = new BinaryProtocolHandlerConfigImpl();
        
        
        DefaultBinaryProtocol defaultBinaryProtocol = webocketPojo.getAnnotation(DefaultBinaryProtocol.class);
        if (defaultBinaryProtocol != null) {
            String beanName = defaultBinaryProtocol.bean();
            if (beanName.isEmpty()) {
                Key<? extends BinaryProtocolHandler> bindKey = Key.get(defaultBinaryProtocol.value());        
                protocolHandlerConfigImpl.setDefaultProtocol(injector.getInstance(bindKey));
            } else {
                Key<? extends BinaryProtocolHandler> bindKey = Key.get(defaultBinaryProtocol.value(), Names.named(beanName));                      
                protocolHandlerConfigImpl.setDefaultProtocol(injector.getInstance(bindKey));
            }
        }
        Map<String, ProtocolHandler<byte[]>> binaryProtocolMap = new HashMap<String, ProtocolHandler<byte[]>>();
        BinaryProtocols binaryProtocols = webocketPojo.getAnnotation(BinaryProtocols.class);
        if (binaryProtocols != null) {
            for (BinaryProtocol binaryProtocol : binaryProtocols.value()) {
                String beanName = binaryProtocol.bean();
                if (beanName.isEmpty()) {
                    Key<? extends BinaryProtocolHandler> bindKey = Key.get(binaryProtocol.handler());                                      
                    binaryProtocolMap.put(binaryProtocol.protocol(), injector.getInstance(bindKey));
                } else {
                    Key<? extends BinaryProtocolHandler> bindKey = Key.get(binaryProtocol.handler(), Names.named(beanName));                                      
                    binaryProtocolMap.put(binaryProtocol.protocol(), injector.getInstance(bindKey));
                }
               
            }
        }
        protocolHandlerConfigImpl.setProtocolHandlers(binaryProtocolMap);
        return protocolHandlerConfigImpl;
    }

    private void setAllowedOrigin(Class<?> webocketPojo, WebSocketServletConfigImpl config) {
        AllowedOrigin allowedOrigin = webocketPojo.getAnnotation(AllowedOrigin.class);
        if (allowedOrigin != null) {
            Set<String> originWhitelist = new HashSet<String>();
            originWhitelist.addAll(Arrays.asList(allowedOrigin.value()));
            config.setOriginWhitelist(originWhitelist);
        }
    }

    private void setClientIdGenerator(Class<?> webocketPojo, WebSocketServletConfigImpl config) {
        ClientIdGenerator clientIdGenerator = null;
        GenerateId generateId = webocketPojo.getAnnotation(GenerateId.class);
        
        if (generateId != null) {
            String beanName = generateId.bean();
            
            if (beanName.isEmpty()) {
                Key<? extends ClientIdGenerator> bindKey = Key.get(generateId.value());                                                   
                clientIdGenerator = injector.getInstance(bindKey);
            } else {
                Key<? extends ClientIdGenerator> bindKey = Key.get(generateId.value(), Names.named(beanName));                                                   
                clientIdGenerator = injector.getInstance(bindKey);
            }          
        }
        config.setClientIdGenerator(clientIdGenerator);
    }

   
}

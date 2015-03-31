package org.kasource.web.websocket.spring.config.loader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.ClientConfigImpl;
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
import org.kasource.web.websocket.config.loader.ClientAnnotationConfigurationBuilder;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringWebSocketServletConfigBuilder implements ClientAnnotationConfigurationBuilder, ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    public ClientConfigImpl configure(Class<?> webocketPojo) {
        ClientConfigImpl config = new ClientConfigImpl();

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
                provider = applicationContext.getBean(auth.value());
            } else {
                provider = applicationContext.getBean(beanName, auth.value());
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
                protocolHandlerConfigImpl.setDefaultProtocol(applicationContext.getBean(defaultTextProtocol.value()));
            } else {
                protocolHandlerConfigImpl.setDefaultProtocol(applicationContext.getBean(beanName, defaultTextProtocol.value()));
            }
        }
        Map<String, ProtocolHandler<String>> textProtocolMap = new HashMap<String, ProtocolHandler<String>>();
        TextProtocols textProtocols = webocketPojo.getAnnotation(TextProtocols.class);
        if (textProtocols != null) {
            for (TextProtocol textProtocol : textProtocols.value()) {
                String beanName = textProtocol.bean();
                if (beanName.isEmpty()) {
                    textProtocolMap.put(textProtocol.protocol(), applicationContext.getBean(textProtocol.handler()));
                } else {
                    textProtocolMap.put(textProtocol.protocol(), applicationContext.getBean(beanName, textProtocol.handler()));
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
                protocolHandlerConfigImpl.setDefaultProtocol(applicationContext.getBean(defaultBinaryProtocol.value()));
            } else {
                protocolHandlerConfigImpl.setDefaultProtocol(applicationContext.getBean(beanName, defaultBinaryProtocol.value()));
            }
        }
        Map<String, ProtocolHandler<byte[]>> binaryProtocolMap = new HashMap<String, ProtocolHandler<byte[]>>();
        BinaryProtocols binaryProtocols = webocketPojo.getAnnotation(BinaryProtocols.class);
        if (binaryProtocols != null) {
            for (BinaryProtocol binaryProtocol : binaryProtocols.value()) {
                String beanName = binaryProtocol.bean();
                if (beanName.isEmpty()) {
                    binaryProtocolMap.put(binaryProtocol.protocol(), applicationContext.getBean(binaryProtocol.handler()));
                } else {
                    binaryProtocolMap.put(binaryProtocol.protocol(), applicationContext.getBean(beanName, binaryProtocol.handler()));
                }
               
            }
        }
        protocolHandlerConfigImpl.setProtocolHandlers(binaryProtocolMap);
        return protocolHandlerConfigImpl;
    }

    private void setAllowedOrigin(Class<?> webocketPojo, ClientConfigImpl config) {
        AllowedOrigin allowedOrigin = webocketPojo.getAnnotation(AllowedOrigin.class);
        if (allowedOrigin != null) {
            Set<String> originWhitelist = new HashSet<String>();
            originWhitelist.addAll(Arrays.asList(allowedOrigin.value()));
            config.setOriginWhitelist(originWhitelist);
        }
    }

    private void setClientIdGenerator(Class<?> webocketPojo, ClientConfigImpl config) {
        ClientIdGenerator clientIdGenerator = null;
        GenerateId generateId = webocketPojo.getAnnotation(GenerateId.class);
        
        if (generateId != null) {
            String beanName = generateId.bean();
            
            if (beanName.isEmpty()) {
                clientIdGenerator = applicationContext.getBean(generateId.value());
            } else {
                clientIdGenerator = applicationContext.getBean(beanName, generateId.value());
            }          
        }
        config.setClientIdGenerator(clientIdGenerator);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        
    }
}

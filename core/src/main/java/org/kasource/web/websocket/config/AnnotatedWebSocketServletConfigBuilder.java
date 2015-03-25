package org.kasource.web.websocket.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.annotation.AllowedOrigin;
import org.kasource.web.websocket.config.annotation.BinaryProtocol;
import org.kasource.web.websocket.config.annotation.BinaryProtocols;
import org.kasource.web.websocket.config.annotation.ClientId;
import org.kasource.web.websocket.config.annotation.DefaultBinaryProtocol;
import org.kasource.web.websocket.config.annotation.DefaultTextProtocol;
import org.kasource.web.websocket.config.annotation.TextProtocol;
import org.kasource.web.websocket.config.annotation.TextProtocols;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;

public class AnnotatedWebSocketServletConfigBuilder {
    public WebSocketServletConfig configure(Class<?> webocketPojo) {
        WebSocketServletConfigImpl config = new WebSocketServletConfigImpl();

        WebSocket websocket = webocketPojo.getAnnotation(WebSocket.class);
        if (websocket == null) {
            throw new WebSocketConfigException(webocketPojo + " must be annotated with " + WebSocket.class);
        }

        config.setDynamicAddressing(websocket.dynamicAddressing());

        setAllowedOrigin(webocketPojo, config);

        setClientIdGenerator(webocketPojo, config);
        
        TextProtocolHandlerConfigImpl textProtocols = getTextProtocols(webocketPojo);
        BinaryProtocolHandlerConfigImpl binaryProtocols = getBinaryProtocols(webocketPojo);
        ProtocolRepositoryImpl protocolRepository = new ProtocolRepositoryImpl(textProtocols, binaryProtocols);
        config.setProtocolRepository(protocolRepository);

        
        
        return config;
    }

    private TextProtocolHandlerConfigImpl getTextProtocols(Class<?> webocketPojo) {
        TextProtocolHandlerConfigImpl textProtocolConfig = new TextProtocolHandlerConfigImpl();
        DefaultTextProtocol defaultTextProtocol = webocketPojo.getAnnotation(DefaultTextProtocol.class);
        if (defaultTextProtocol != null) {
            try {
                textProtocolConfig.setDefaultProtocol(defaultTextProtocol.value().newInstance());
            } catch (Exception e) {
                throw new WebSocketConfigException("Instance of class: " + defaultTextProtocol.value()
                            + " could not be created (missing empty public constuctor). From " + webocketPojo
                            + " annotation " + DefaultTextProtocol.class);
            }
        }
        Map<String, ProtocolHandler<String>> textProtocolMap = new HashMap<String, ProtocolHandler<String>>();
        TextProtocols textProtocols = webocketPojo.getAnnotation(TextProtocols.class);
        if (textProtocols != null) {
            for (TextProtocol textProtocol : textProtocols.value()) {
                try {
                    textProtocolMap.put(textProtocol.protocol(), textProtocol.handler().newInstance());
                } catch (Exception e) {
                    throw new WebSocketConfigException("Instance of class: " + textProtocol.handler()
                                + " could not be created (missing empty public constuctor). From " + webocketPojo
                                + " annotation " + TextProtocol.class);
                }
            }
        }
        textProtocolConfig.setProtocolHandlers(textProtocolMap);
        return textProtocolConfig;
    }
    
    private BinaryProtocolHandlerConfigImpl getBinaryProtocols(Class<?> webocketPojo) {
        BinaryProtocolHandlerConfigImpl binaryProtocolConfig = new BinaryProtocolHandlerConfigImpl();
        DefaultBinaryProtocol defaultBinaryProtocol = webocketPojo.getAnnotation(DefaultBinaryProtocol.class);
        if (defaultBinaryProtocol != null) {
            try {
                binaryProtocolConfig.setDefaultProtocol(defaultBinaryProtocol.value().newInstance());
            } catch (Exception e) {
                throw new WebSocketConfigException("Instance of class: " + defaultBinaryProtocol.value()
                            + " could not be created (missing empty public constuctor). From " + webocketPojo
                            + " annotation " + DefaultBinaryProtocol.class);
            }
        }
        Map<String, ProtocolHandler<byte[]>> binaryProtocolMap = new HashMap<String, ProtocolHandler<byte[]>>();
        BinaryProtocols binaryProtocols = webocketPojo.getAnnotation(BinaryProtocols.class);
        if (binaryProtocols != null) {
            for (BinaryProtocol binaryProtocol : binaryProtocols.value()) {
                try {
                    binaryProtocolMap.put(binaryProtocol.protocol(), binaryProtocol.handler().newInstance());
                } catch (Exception e) {
                    throw new WebSocketConfigException("Instance of class: " + binaryProtocol.handler()
                                + " could not be created (missing empty public constuctor). From " + webocketPojo
                                + " annotation " + BinaryProtocol.class);
                }
            }
        }
        binaryProtocolConfig.setProtocolHandlers(binaryProtocolMap);
        return binaryProtocolConfig;
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
        ClientId clientId = webocketPojo.getAnnotation(ClientId.class);

        if (clientId != null) {
            config.setClientIdGenerator(new DefaultClientIdGenerator());
            Class<? extends ClientIdGenerator> idGeneratorClass = clientId.value();
            try {
                config.setClientIdGenerator(idGeneratorClass.newInstance());
            } catch (Exception e) {
                throw new WebSocketConfigException("Instance of class: " + idGeneratorClass
                            + " could not be created (missing empty public constuctor). From " + webocketPojo
                            + " annotation " + ClientId.class);
            }
        }
    }
}

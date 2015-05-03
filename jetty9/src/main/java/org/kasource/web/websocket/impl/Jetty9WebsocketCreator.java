package org.kasource.web.websocket.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.kasource.commons.reflection.filter.FieldFilterBuilder;
import org.kasource.commons.reflection.util.FieldUtils;
import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.EndpointConfig;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.servlet.HttpServletHandshakeRequest;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jetty9WebsocketCreator implements WebSocketCreator {
    private static final Logger LOG = LoggerFactory.getLogger(Jetty9WebsocketCreator.class);
    private static Field requestField;
    
    static {
        Set<Field> requestFields = FieldUtils.getDeclaredFields(ServletUpgradeRequest.class, new FieldFilterBuilder().extendsType(HttpServletRequest.class).build());
        if (requestFields.isEmpty()) {
            throw new IllegalStateException("Could not find field for HttpServletRequest for class " + ServletUpgradeRequest.class);
        }
        requestField = requestFields.iterator().next();
        requestField.setAccessible(true);
    }
    
    private EndpointConfig endpointConfig;
    private ServletConfigUtil configUtil; 
    
    public Jetty9WebsocketCreator(ServletConfigUtil configUtil, 
                EndpointConfig endpointConfig) {
        this.configUtil = configUtil;
        this.endpointConfig = endpointConfig;
    }
    
    @Override
    public Object createWebSocket(UpgradeRequest request, UpgradeResponse response) {
        HttpServletRequest httpRequest = getHttpRequest(request);
        HandshakeRequest handshakeRequest = new HttpServletHandshakeRequest(httpRequest);
        
        if (!verifyOrigin(request.getOrigin())) {
            response.setSuccess(false);
            try {
                response.sendForbidden("Invalid origin " + request.getOrigin().toString());
            } catch (IOException e) {
               response.setStatusCode(HttpStatus.FORBIDDEN_403);
            }
            return null;
        }
        
        String subProtocol = selectSubProtocol(request.getSubProtocols());
        response.setAcceptedSubProtocol(subProtocol);
              
        String url = configUtil.getMaping();
        
        if (endpointConfig.isDynamicAddressing()) {
            url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        }
        
        ClientChannel clientChannel = endpointConfig.getClientChannelFor(url);
        String username = null;
        try {
             username = clientChannel.authenticate(endpointConfig.getAuthenticationProvider(), handshakeRequest);
        } catch (AuthenticationException e) {
            LOG.warn("Unauthorized access for " + httpRequest.getRemoteHost(), e);
            try {
                response.sendForbidden("Unauthorized access");
            } catch (IOException ioe) {
               response.setStatusCode(HttpStatus.FORBIDDEN_403);
            }
            return null;
        }
    
        WebSocketClientConfig clientConfiguration = endpointConfig.getClientBuilder(clientChannel).get(handshakeRequest)
                                                        .url(url)
                                                        .username(username)
                                                        .protocol(subProtocol, endpointConfig.getProtocolRepository())
                                                        .build();
        
        return new Jetty9WebsocketClient(clientConfiguration);
    }
    
    private boolean verifyOrigin(String origin) {
        boolean validOrigin = endpointConfig.isValidOrigin(origin);
        if (!validOrigin) {
            LOG.warn("Invalid origin: " + origin +" in connection attempt");
        }
        return validOrigin;
        
    }
    
    private String selectSubProtocol(List<String> subProtocols) {     
        for (String clientProtocol : subProtocols) {
            if (endpointConfig.getProtocolRepository().hasProtocol(clientProtocol)) {
                LOG.info("Requested protocol "+ clientProtocol + " found by server");
                return clientProtocol;
            }
        }
        LOG.info("Protocols " + subProtocols + " requested by client, but no such protocols found on server.");
        return null;
    }
    
    private HttpServletRequest getHttpRequest(UpgradeRequest request)  {       
        try {
            return (HttpServletRequest) requestField.get(request);    
        } catch (Exception e) {
            throw new IllegalStateException("Could not get HTTP request from field", e);
        }
    }

}

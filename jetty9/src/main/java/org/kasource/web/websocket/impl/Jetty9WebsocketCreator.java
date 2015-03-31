package org.kasource.web.websocket.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.ClientConfig;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jetty9WebsocketCreator implements WebSocketCreator {
    private static final Logger LOG = LoggerFactory.getLogger(Jetty9WebsocketCreator.class);
    private ClientConfig clientConfig;
    private ServletConfigUtil configUtil; 
    
    public Jetty9WebsocketCreator(ServletConfigUtil configUtil, 
                                  ClientConfig clientConfig) {
        this.configUtil = configUtil;
        this.clientConfig = clientConfig;
    }
    
    @Override
    public Object createWebSocket(UpgradeRequest request, UpgradeResponse response) {
        HttpServletRequest httpRequest = getHttpRequest(request);
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
        
        if (clientConfig.isDynamicAddressing()) {
            url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        }
        
        ClientChannel clientChannel = clientConfig.getClientChannelFor(url);
        String username = null;
        try {
             username = clientChannel.authenticate(clientConfig.getAuthenticationProvider(), httpRequest);
        } catch (AuthenticationException e) {
            LOG.warn("Unauthorized access for " + httpRequest.getRemoteHost(), e);
            try {
                response.sendForbidden("Unauthorized access");
            } catch (IOException ioe) {
               response.setStatusCode(HttpStatus.FORBIDDEN_403);
            }
            return null;
        }
    
        WebSocketClientConfig clientConfiguration = clientConfig.getClientBuilder(clientChannel).get(httpRequest)
                                                        .url(url)
                                                        .username(username)
                                                        .protocol(subProtocol, clientConfig.getProtocolRepository())
                                                        .build();
        
        return new Jetty9WebsocketClient(clientConfiguration);
    }
    
    private boolean verifyOrigin(String origin) {
        boolean validOrigin = clientConfig.isValidOrigin(origin);
        if (!validOrigin) {
            LOG.warn("Invalid origin: " + origin +" in connection attempt");
        }
        return validOrigin;
        
    }
    
    private String selectSubProtocol(List<String> subProtocols) {     
        for (String clientProtocol : subProtocols) {
            if (clientConfig.getProtocolRepository().hasProtocol(clientProtocol)) {
                LOG.info("Requested protocol "+ clientProtocol + " found by server");
                return clientProtocol;
            }
        }
        LOG.info("Protocols " + subProtocols + " requested by client, but no such protocols found on server.");
        return null;
    }
    
    private HttpServletRequest getHttpRequest(UpgradeRequest request)  {
        
        try {
            ServletUpgradeRequest servletRequest = (ServletUpgradeRequest) request;
            Field[] fields = ServletUpgradeRequest.class.getDeclaredFields();
            for (Field field : fields) {     
                if (HttpServletRequest.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return (HttpServletRequest) field.get(servletRequest);
                }
            }
            throw new IllegalStateException("Could not find field for HttpServletRequest");
        } catch (ClassCastException e) {
            throw new IllegalStateException("Could not cast to ServletUpgradeRequest", e);
        } catch (Exception e) {
            throw new IllegalStateException("Could not get HTTP request from field", e);
        }
    }

}

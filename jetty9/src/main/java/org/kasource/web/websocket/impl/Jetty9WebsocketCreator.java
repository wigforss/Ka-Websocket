package org.kasource.web.websocket.impl;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jetty9WebsocketCreator implements WebSocketCreator {
    private static final Logger LOG = LoggerFactory.getLogger(Jetty9WebsocketCreator.class);
    private WebSocketServletConfig webSocketServletConfig;
    private ServletConfigUtil configUtil; 
    
    public Jetty9WebsocketCreator(ServletConfigUtil configUtil, 
                                  WebSocketServletConfig webSocketServletConfig) {
        this.configUtil = configUtil;
        this.webSocketServletConfig = webSocketServletConfig;
    }
    
    @Override
    public Object createWebSocket(UpgradeRequest request, UpgradeResponse response) {
        HttpServletRequest httpRequest = getHttpRequest(request);
        if (!verifyOrigin(request.getOrigin())) {
            return null;
        }
        
        String subProtocol = selectSubProtocol(request.getSubProtocols());
        response.setAcceptedSubProtocol(subProtocol);
              
        String url = configUtil.getMaping();
        
        if (webSocketServletConfig.isDynamicAddressing()) {
            url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        }
        
        WebSocketManager manager = webSocketServletConfig.getWebSocketManager(url);
        String username = null;
        try {
             username = manager.authenticate(httpRequest);
        } catch (AuthenticationException e) {
            LOG.error("Unauthorized access for " + httpRequest.getRemoteHost(), e);
            return null;
        }
    
        WebSocketClientConfig clientConfig = webSocketServletConfig.getClientBuilder(manager).get(httpRequest)
                                                        .url(url)
                                                        .username(username)
                                                        .subProtocol(subProtocol)
                                                        .build();
        
        return new Jetty9WebsocketClient(clientConfig);
    }
    
    private boolean verifyOrigin(String origin) {
        boolean validOrigin = webSocketServletConfig.isValidOrigin(origin);
        LOG.warn("Invalid origin: " + origin +" in connection attempt");
        return validOrigin;
        
    }
    
    private String selectSubProtocol(List<String> subProtocols) {     
        for (String clientProtocol : subProtocols) {
            if(webSocketServletConfig.hasProtocol(clientProtocol, configUtil.getMaping())) {
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
            Field field = ServletUpgradeRequest.class.getDeclaredField("req");
            field.setAccessible(true);
            return (HttpServletRequest) field.get(servletRequest);
        } catch (ClassCastException e) {
            throw new IllegalStateException("Could not cast to ServletUpgradeRequest", e);
        } catch (Exception e) {
            throw new IllegalStateException("Could not get HTTP request from field named req", e);
        }
    }

}

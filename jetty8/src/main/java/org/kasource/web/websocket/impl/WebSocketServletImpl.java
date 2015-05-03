package org.kasource.web.websocket.impl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.EndpointConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.servlet.HttpServletHandshakeRequest;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class WebSocketServletImpl extends WebSocketServlet {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServletImpl.class);
    private static final long serialVersionUID = 1L;
  
    
    private ServletConfigUtil configUtil; 
 
    private EndpointConfig endpointConfig;
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        configUtil = new ServletConfigUtil(getServletConfig());
        endpointConfig = configUtil.getConfiguration();
           
        configUtil.validateMapping(endpointConfig.isDynamicAddressing());
        if (!endpointConfig.isDynamicAddressing()) {
            endpointConfig.getClientChannelFor(configUtil.getMaping());
        }
        LOG.info("Initialization completed.");
    }

    
    
    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        HandshakeRequest handshakeRequest = new HttpServletHandshakeRequest(request);
        ProtocolRepository protocolRepository = endpointConfig.getProtocolRepository();
        
        // Only accept protocols configured
        if (protocol != null && !protocolRepository.hasProtocol(protocol)) {
            return null;
        }
        
        String url = configUtil.getMaping();
        if (endpointConfig.isDynamicAddressing()) {
            url = request.getRequestURI().substring(request.getContextPath().length());
        }
        
        ClientChannel clientChannel =  endpointConfig.getClientChannelFor(url);
        
        String username = null;
        try {
             username = clientChannel.authenticate(endpointConfig.getAuthenticationProvider(), handshakeRequest);
        } catch (AuthenticationException e) {
            LOG.error("Unauthorized access for " + request.getRemoteHost(), e);
            throw e;
        }
        
       
        
        WebSocketClientConfig clientConfiguration =  endpointConfig.getClientBuilder(clientChannel).get(handshakeRequest)
                                                    .url(url)
                                                    .username(username)
                                                    .protocol(protocol, protocolRepository)
                                                    .build();
         
        Jetty8WebSocketClient client = new Jetty8WebSocketClient(clientConfiguration);
        LOG.info("Client connecion created for " + request.getRemoteHost() + " with username " + username + " and ID " + clientConfiguration.getClientId() + " on " + url);
        return client;
    }


    
    /**
     * 
     * Example: The origin white list for the example.com Web server could be the strings 
     * "http://example.com", "https://example.com", "http://www.example.com", and "https://www.example.com". 
     *
     * @param origin    The value of the origin header from the request which
     *                  may be <code>null</code>
     *
     * @return  <code>true</code> to accept the request. <code>false</code> to
     *          reject it. This default implementation always returns
     *          <code>true</code>.
     */
    @Override
    public boolean checkOrigin(HttpServletRequest request, String origin) {
        boolean validOrigin = endpointConfig.isValidOrigin(origin);
        if (!validOrigin) {
            LOG.warn("Invalid origin: " + origin +" in connection attempt");
        }
        return validOrigin;
    }

   
}

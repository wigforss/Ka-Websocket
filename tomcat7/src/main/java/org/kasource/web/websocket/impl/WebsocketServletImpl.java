
package org.kasource.web.websocket.impl;



import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.EndpointConfig;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.servlet.HttpServletHandshakeRequest;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class WebsocketServletImpl extends WebSocketServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(WebsocketServletImpl.class);
   
    private ServletConfigUtil configUtil; 
    private EndpointConfig endpointConfig;
 
    private int outboundByteBufferSize;

    private int outboundCharBufferSize;
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        configUtil = new ServletConfigUtil(getServletConfig());
        endpointConfig = configUtil.getConfiguration();
     
        
        outboundByteBufferSize = configUtil.parseInitParamAsInt("outboundByteBufferSize");
        outboundCharBufferSize = configUtil.parseInitParamAsInt("outboundCharBufferSize");
       
        configUtil.validateMapping(endpointConfig.isDynamicAddressing());
        if (!endpointConfig.isDynamicAddressing()) {
            endpointConfig.getClientChannelFor(configUtil.getMaping());
           
        } 
        LOG.info("Initialization completed.");
    }

  
    

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
            HandshakeRequest handshakeRequest = new HttpServletHandshakeRequest(request);
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
        
            
            WebSocketClientConfig clientConfiguration = endpointConfig.getClientBuilder(clientChannel).get(handshakeRequest)
                                                    .url(url)
                                                    .username(username)
                                                    .protocol(subProtocol, endpointConfig.getProtocolRepository())
                                                    .build();
            Tomcat7WebSocketClient client = new Tomcat7WebSocketClient(clientConfiguration);
   
        
            if (outboundByteBufferSize != 0) {
                client.setOutboundByteBufferSize(outboundByteBufferSize);
            }
            if (outboundCharBufferSize != 0) {
                client.setOutboundCharBufferSize(outboundCharBufferSize);
            }
            LOG.info("Client connecion created for " + request.getRemoteHost() + " with username " + username + " and ID " + clientConfiguration.getClientId() + " on " + url);
            return client;
        
    }
    
    
   

    /**
     * Intended to be overridden by sub-classes that wish to verify the origin
     * of a WebSocket request before processing it.
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
    protected boolean verifyOrigin(String origin) {
        boolean validOrigin = endpointConfig.isValidOrigin(origin);
        LOG.warn("Invalid origin: " + origin +" in connection attempt");
        return validOrigin;
        
    }
    
    protected String selectSubProtocol(List<String> subProtocols) {     
        for (String clientProtocol : subProtocols) {
            if (endpointConfig.getProtocolRepository().hasProtocol(clientProtocol)) {
                LOG.info("Requested protocol "+ clientProtocol + " found by server");
                return clientProtocol;
            }
        }
        LOG.info("Protocols " + subProtocols + " requested by client, but no such protocols found on server.");
        return null;
    }
}

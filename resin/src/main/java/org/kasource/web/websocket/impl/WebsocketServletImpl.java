
package org.kasource.web.websocket.impl;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.EndpointConfig;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.servlet.HttpServletHandshakeRequest;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.websocket.WebSocketListener;
import com.caucho.websocket.WebSocketServletRequest;



public class WebsocketServletImpl extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(WebsocketServletImpl.class);
    private static final long serialVersionUID = 1L;
   

    private ServletConfigUtil configUtil; 
   
    private EndpointConfig endpointConfig;


    @Override
    public void init() throws ServletException {
        super.init();
        configUtil = new ServletConfigUtil(getServletConfig());
        endpointConfig = configUtil.getConfiguration();
       
       
        configUtil.validateMapping(endpointConfig.isDynamicAddressing());
        if(!endpointConfig.isDynamicAddressing()) {
            endpointConfig.getClientChannelFor(configUtil.getMaping());
        }
        LOG.info("Initialization completed.");
    }

   

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {    
        String origin = req.getHeader("Sec-WebSocket-Origin");
        if (!validOrigin(origin)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String protocols = req.getHeader("Sec-WebSocket-Protocol");
        String subProtocol = null;
        if (protocols != null) {
            subProtocol = selectSubProtocol(protocols.split(","));
        }

        if (subProtocol != null) {
            res.setHeader("Sec-WebSocket-Protocol", subProtocol);
        }

        WebSocketServletRequest wsReq = (WebSocketServletRequest) req;

        wsReq.startWebSocket(createClient(req, subProtocol));
    }



    private String selectSubProtocol(String[] subProtocols) {
        for (String clientProtocol : subProtocols) {
            if(endpointConfig.getProtocolRepository().hasProtocol(clientProtocol)) {
                return clientProtocol;
            }
        }
        return null;
    }



    private boolean validOrigin(String origin) {
        boolean validOrigin = endpointConfig.isValidOrigin(origin);;
        LOG.warn("Invalid origin: " + origin +" in connection attempt");
        return validOrigin;
    }



    private WebSocketListener createClient(HttpServletRequest request, String protocol) {
        HandshakeRequest handshakeRequest = new HttpServletHandshakeRequest(request);
        
        String url = configUtil.getMaping();
        
        if (endpointConfig.isDynamicAddressing()) {
            url = request.getRequestURI().substring(request.getContextPath().length());
        }
        ClientChannel clientChannel = endpointConfig.getClientChannelFor(url);
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
                                                .protocol(protocol, endpointConfig.getProtocolRepository())
                                                .build();
        
        ResinWebSocketClient client = new ResinWebSocketClient(clientConfiguration);
        LOG.info("Client connecion created for " + request.getRemoteHost() + " with username " + username + " and ID " + clientConfiguration.getClientId() + " on " + url);
        return client;
    }

   
    



 

}

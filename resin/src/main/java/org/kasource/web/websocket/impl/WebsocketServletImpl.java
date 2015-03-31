
package org.kasource.web.websocket.impl;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.ClientConfig;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.websocket.WebSocketListener;
import com.caucho.websocket.WebSocketServletRequest;



public class WebsocketServletImpl extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(WebsocketServletImpl.class);
    private static final long serialVersionUID = 1L;
   

    private ServletConfigUtil configUtil; 
   
    private ClientConfig clientConfig;


    @Override
    public void init() throws ServletException {
        super.init();
        configUtil = new ServletConfigUtil(getServletConfig());
        clientConfig = configUtil.getConfiguration();
       
       
        configUtil.validateMapping(clientConfig.isDynamicAddressing());
        if(!clientConfig.isDynamicAddressing()) {
            clientConfig.getClientChannelFor(configUtil.getMaping());
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
            if(clientConfig.getProtocolRepository().hasProtocol(clientProtocol)) {
                return clientProtocol;
            }
        }
        return null;
    }



    private boolean validOrigin(String origin) {
        boolean validOrigin = clientConfig.isValidOrigin(origin);;
        LOG.warn("Invalid origin: " + origin +" in connection attempt");
        return validOrigin;
    }



    private WebSocketListener createClient(HttpServletRequest request, String protocol) {
        String url = configUtil.getMaping();
        
        if (clientConfig.isDynamicAddressing()) {
            url = request.getRequestURI().substring(request.getContextPath().length());
        }
        ClientChannel clientChannel = clientConfig.getClientChannelFor(url);
        String username = null;
        try {
             username = clientChannel.authenticate(clientConfig.getAuthenticationProvider(), request);
        } catch (AuthenticationException e) {
            LOG.error("Unauthorized access for " + request.getRemoteHost(), e);
            throw e;
        }
        
        WebSocketClientConfig clientConfiguration = clientConfig.getClientBuilder(clientChannel).get(request)
                                                .url(url)
                                                .username(username)
                                                .protocol(protocol, clientConfig.getProtocolRepository())
                                                .build();
        
        ResinWebSocketClient client = new ResinWebSocketClient(clientConfiguration);
        LOG.info("Client connecion created for " + request.getRemoteHost() + " with username " + username + " and ID " + clientConfiguration.getClientId() + " on " + url);
        return client;
    }

   
    



 

}

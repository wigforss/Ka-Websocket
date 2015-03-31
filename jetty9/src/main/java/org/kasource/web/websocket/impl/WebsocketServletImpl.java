package org.kasource.web.websocket.impl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.kasource.web.websocket.config.ClientConfig;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet
public class WebsocketServletImpl extends WebSocketServlet {
    private static final Logger LOG = LoggerFactory.getLogger(WebsocketServletImpl.class);
    private static final long serialVersionUID = 1L;
    private ServletConfigUtil configUtil; 
    
    private ClientConfig clientConfig;
    
    @Override
    public void init() throws ServletException {
        configUtil = new ServletConfigUtil(getServletConfig());
        clientConfig = configUtil.getConfiguration();
       
        
        configUtil.validateMapping(clientConfig.isDynamicAddressing());
        if (!clientConfig.isDynamicAddressing()) {
            clientConfig.getClientChannelFor(configUtil.getMaping());
        }
        LOG.info("Initialization completed.");
        super.init();
    }
    
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10000);
        factory.setCreator(new Jetty9WebsocketCreator(configUtil, clientConfig));
    }

}

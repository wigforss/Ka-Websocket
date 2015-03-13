package org.kasource.web.websocket.impl;

import javax.servlet.ServletException;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.impl.jetty9.Jetty9WebsocketCreator;
import org.kasource.web.websocket.impl.jetty9.Jetty9WebsocketImpl;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketServletImpl extends WebSocketServlet {
    private static final Logger LOG = LoggerFactory.getLogger(WebsocketServletImpl.class);
    private static final long serialVersionUID = 1L;
    private ServletConfigUtil configUtil; 
    
    private WebSocketServletConfig webSocketServletConfig;
    
    @Override
    public void init() throws ServletException {
        configUtil = new ServletConfigUtil(getServletConfig());
        webSocketServletConfig = configUtil.getConfiguration();
       
        
        configUtil.validateMapping(webSocketServletConfig.isDynamicAddressing());
        if (!webSocketServletConfig.isDynamicAddressing()) {
            webSocketServletConfig.getWebSocketManager(configUtil.getMaping());
        }
        LOG.info("Initialization completed.");
        super.init();
    }
    
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(10000);
        factory.setCreator(new Jetty9WebsocketCreator(configUtil, webSocketServletConfig));
    }

}

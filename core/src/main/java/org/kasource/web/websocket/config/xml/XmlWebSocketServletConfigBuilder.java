package org.kasource.web.websocket.config.xml;

import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.security.AuthenticationProvider;

public class XmlWebSocketServletConfigBuilder {

   
    private ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();
    private WebSocketManagerRepository managerRepository;
    private ProtocolRepository protocolRepository;
    private Set<String> originWhitelist;
    private AuthenticationProvider authenticationProvider;
    private XmlWebsocketServlet servletConfig;
    
    public XmlWebSocketServletConfigBuilder(XmlWebsocketServlet servletConfig) {
        this.servletConfig = servletConfig;
        
    }
    
    public WebSocketServletConfig build() {
        WebSocketServletConfigImpl config = new WebSocketServletConfigImpl();
        if (servletConfig.getAuthenticationProvider() != null) {
            config.getAuthenticationProvider();
        } else {
            config.setAuthenticationProvider(authenticationProvider);
        }
        if (servletConfig.getClientIdGenerator() != null) {
            config.setClientIdGenerator(servletConfig.getClientIdGenerator());
        } else {
            config.setClientIdGenerator(clientIdGenerator);
        }
       
        if (servletConfig.getOriginWhitelist() != null && !servletConfig.getOriginWhitelist().isEmpty()) {
            config.setOriginWhitelist(servletConfig.getOriginWhitelist());
        } else {
            config.setOriginWhitelist(originWhitelist);
        }
        if (servletConfig.getProtocolRepository() != null) {
            config.setProtocolRepository(servletConfig.getProtocolRepository());
        } else {
            config.setProtocolRepository(protocolRepository);
        }
        
        config.setDynamicAddressing(servletConfig.isDynamicAddressing() == Boolean.TRUE);
        config.setManagerRepository(managerRepository);       
        config.setServletName(servletConfig.getServletName());
        
        return config;
    }
    
    
    
    public XmlWebSocketServletConfigBuilder originWhitelist(Set<String> originWhitelist) {
        this.originWhitelist = originWhitelist;
        return this;
    }
    
    public XmlWebSocketServletConfigBuilder managerRepository(WebSocketManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
        return this;
    }
    
    public XmlWebSocketServletConfigBuilder protocolRepository(ProtocolRepository protocolRepository) {
      this.protocolRepository = protocolRepository; 
      return this;
    }
    
   
    
   

}

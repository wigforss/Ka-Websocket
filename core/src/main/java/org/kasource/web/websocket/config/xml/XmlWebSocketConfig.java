package org.kasource.web.websocket.config.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.config.xml.jaxb.Websocket;
import org.kasource.web.websocket.config.xml.jaxb.WebsocketConfig;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.manager.WebSocketManagerRepositoryImpl;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;

public class XmlWebSocketConfig implements WebSocketConfig {

    private WebsocketConfig config;
    private ClientIdGenerator idGenerator = new DefaultClientIdGenerator();
    private Set<String> originWhitelist = new HashSet<String>();
    private Map<String, WebSocketServletConfig> servletConfigs = new HashMap<String, WebSocketServletConfig>();
    private ProtocolRepository protocolRepository;
    private WebSocketManagerRepositoryImpl managerRepository;
    private WebSocketChannelFactory channelFactory;
    private WebSocketListenerRegister listenerRegister;
   
    public XmlWebSocketConfig(WebsocketConfig config, ServletContext servletContext) {
        this.config = config;
        initialize(servletContext);
    }
    
   
    
    private void initialize(ServletContext servletContext) {
        loadProtocols();
        
        channelFactory = (WebSocketChannelFactory) servletContext.getAttribute(WebSocketChannelFactory.class.getName());
        XmlAuthentication authentication = null;
        if (config.getAuthenticationProvider() != null) {
            authentication = new XmlAuthentication(config.getAuthenticationProvider());
        }
       
        if (config.getClientIdGenerator() != null) {
            idGenerator = new XmlClientIdGenerator(config.getClientIdGenerator()).getClientIdGenerator();
        }
        
        
        if (config.getOriginWhitelist() != null) {
            originWhitelist.addAll(config.getOriginWhitelist().getOrigin());
        }
        
        managerRepository = new WebSocketManagerRepositoryImpl(servletContext);
        
        if (authentication != null) {
            managerRepository.setDefaultAuthenticationProvider(authentication.getAutenticationProvider());
        }
        
      
        loadServletConfigs(idGenerator, authentication);
        listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
    }
    
    private void loadServletConfigs(ClientIdGenerator idGenerator, XmlAuthentication authentication) {
        for(Websocket websocket :config.getWebsocket()) {
            WebSocketServletConfig servletConfig = new XmlWebSocketServletConfigBuilder(new XmlWebsocketServlet(websocket))
                                                            .protocolRepository(protocolRepository)
                                                            .managerRepository(managerRepository)
                                                            .originWhitelist(originWhitelist)
                                                            .build();
                        
                      
            
            servletConfigs.put(servletConfig.getServletName(), servletConfig);
        }
    }

    private void loadProtocols() {
        ProtocolHandlerConfig<String> textProtocolHandlerConfig = null;
        ProtocolHandlerConfig<byte[]> binaryProtocolHandlerConfig = null;
        if(config.getTextProtocolHandler() != null) {
            textProtocolHandlerConfig = new XmlTextProtocolHandlerConfig(config.getTextProtocolHandler());
        }
        
        if(config.getBinaryProtocolHandler() != null) {
            binaryProtocolHandlerConfig = new XmlBinaryProtocolHandlerConfig(config.getBinaryProtocolHandler());
        }
        
        try {
            protocolRepository = new ProtocolRepositoryImpl(textProtocolHandlerConfig, binaryProtocolHandlerConfig); 
        } catch (Exception e) {
           throw new WebSocketConfigException("Could not load Protocol Handlers", e);
        }
    }
    
   
    
   

  

    @Override
    public ProtocolRepository getProtocolRepository() {
        return protocolRepository;
    }
    
    @Override
    public Set<String> getOriginWhitelist() {
        
        return originWhitelist;
    }


    @Override
    public WebSocketManagerRepository getManagerRepository() {
        return managerRepository;
    }

    @Override
    public WebSocketChannelFactory getChannelFactory() {
        return channelFactory;
    }

    @Override
    public WebSocketServletConfig getServletConfig(String servletName) {
        return servletConfigs.get(servletName);
    }



    @Override
    public WebSocketListenerRegister getListenerRegister() {
        return listenerRegister;
    }



    @Override
    public void registerServlet(WebSocketServletConfigImpl servlet) {
        
    }

}

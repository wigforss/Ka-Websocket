package org.kasource.web.websocket.config.xml;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.xml.bind.annotation.XmlAttribute;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.jaxb.Websocket;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.security.AuthenticationProvider;



public class XmlWebsocketServlet {
    
    private Set<String> originWhitelist = new HashSet<String>();
    private ClientIdGenerator clientIdGenerator;
    private AuthenticationProvider authenticationProvider;
    private ProtocolRepository protocolRepository;
    private String servletName;
   
    protected Boolean dynamicAddressing;
    private Websocket websocketServletConfig;
    
    public XmlWebsocketServlet(Websocket websocketServletConfig) {
        this.websocketServletConfig = websocketServletConfig;
        initialize();
    }
    
    private void initialize() {
        loadProtocols();
        
     
        if (websocketServletConfig.getAuthenticationProvider() != null) {
            XmlAuthentication authentication = new XmlAuthentication(websocketServletConfig.getAuthenticationProvider());
            authenticationProvider = authentication.getAutenticationProvider();
        }
       
        if (websocketServletConfig.getClientIdGenerator() != null) {
            clientIdGenerator = new XmlClientIdGenerator(websocketServletConfig.getClientIdGenerator()).getClientIdGenerator();
        }
        
        
        if (websocketServletConfig.getOriginWhitelist() != null) {
            originWhitelist.addAll(websocketServletConfig.getOriginWhitelist().getOrigin());
        }
        
        servletName = websocketServletConfig.getServletName();
        dynamicAddressing = websocketServletConfig.isDynamicAddressing();
        
    }
    
    private void loadProtocols() {
        ProtocolHandlerConfig<String> textProtocolHandlerConfig = null;
        ProtocolHandlerConfig<byte[]> binaryProtocolHandlerConfig = null;
        if (websocketServletConfig.getTextProtocolHandler() != null) {
            textProtocolHandlerConfig = new XmlTextProtocolHandlerConfig(websocketServletConfig.getTextProtocolHandler());
        }
        
        if (websocketServletConfig.getBinaryProtocolHandler() != null) {
            binaryProtocolHandlerConfig = new XmlBinaryProtocolHandlerConfig(websocketServletConfig.getBinaryProtocolHandler());
        }
        
        try {
            protocolRepository = new ProtocolRepositoryImpl(textProtocolHandlerConfig, binaryProtocolHandlerConfig); 
        } catch (Exception e) {
           throw new WebSocketConfigException("Could not load Protocol Handlers", e);
        }
    }

    /**
     * @return the originWhitelist
     */
    public Set<String> getOriginWhitelist() {
        return originWhitelist;
    }

    /**
     * @return the clientIdGenerator
     */
    public ClientIdGenerator getClientIdGenerator() {
        return clientIdGenerator;
    }

    /**
     * @return the authenticationProvider
     */
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    /**
     * @return the protocolRepository
     */
    public ProtocolRepository getProtocolRepository() {
        return protocolRepository;
    }

    /**
     * @return the servletName
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * @return the dynamicAddressing
     */
    public Boolean isDynamicAddressing() {
        return dynamicAddressing;
    }

    /**
     * @return the websocketServletConfig
     */
    public Websocket getWebsocketServletConfig() {
        return websocketServletConfig;
    }
}

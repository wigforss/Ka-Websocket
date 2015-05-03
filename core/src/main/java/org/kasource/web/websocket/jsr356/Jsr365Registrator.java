package org.kasource.web.websocket.jsr356;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import org.kasource.web.websocket.config.EndpointConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.annotation.BinaryProtocol;
import org.kasource.web.websocket.config.annotation.BinaryProtocols;
import org.kasource.web.websocket.config.annotation.TextProtocol;
import org.kasource.web.websocket.config.annotation.TextProtocols;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.config.loader.EndpointAnnotationConfigurationBuilder;
import org.kasource.web.websocket.register.EndpointRegistrator;

public class Jsr365Registrator implements EndpointRegistrator {
    private EndpointAnnotationConfigurationBuilder configurationBuilder;
    private ServletContext servletContext;
    
    public Jsr365Registrator(ServletContext servletContext, 
                             EndpointAnnotationConfigurationBuilder configurationBuilder) {
         this.servletContext = servletContext;
         this.configurationBuilder = configurationBuilder;
    }
    
    public void register(Class<?> webSocketPojo) {
        ServerContainer container = (ServerContainer) servletContext.getAttribute("javax.websocket.server.ServerContainer");
        
        EndpointConfigImpl clientConfig = configurationBuilder.configure(webSocketPojo);
        WebSocketConfig config = (WebSocketConfig) servletContext.getAttribute(WebSocketConfig.class.getName());
        config.registerEndpointConfig(clientConfig);
       
        WebSocket annotation = webSocketPojo.getAnnotation(WebSocket.class);
        if (annotation == null) {
            throw new WebSocketConfigException(webSocketPojo + " must be annotated with " + WebSocket.class);
        }
       
        ServerEndpointConfig serverConfig = ServerEndpointConfig.Builder.create(WebSocketClientJsr356.class, annotation.value())
                                                                            .configurator(new EndPointConfigurator(clientConfig))
                                                                            .subprotocols(getSubProtocols(webSocketPojo))
                                                                            .build();
        try {
            container.addEndpoint(serverConfig);
        } catch (DeploymentException e) {
            throw new WebSocketConfigException("Could not add websocket for " + webSocketPojo, e);
        }
    }
    
    private List<String> getSubProtocols(Class<?> webSocketPojo) {
        List<String> protocols = new ArrayList<String>();
        BinaryProtocols binaryProtocols = webSocketPojo.getAnnotation(BinaryProtocols.class);
        if (binaryProtocols != null) {
            for (BinaryProtocol binaryProtocol : binaryProtocols.value()) {
                protocols.add(binaryProtocol.protocol());
            }
        }
        TextProtocols textProtocols = webSocketPojo.getAnnotation(TextProtocols.class);
        if (textProtocols != null) {
            for (TextProtocol textProtocol : textProtocols.value()) {
                protocols.add(textProtocol.protocol());
            }
        }
        return protocols;
            
    }
    
    
    
  
        
    
}



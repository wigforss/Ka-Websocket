package org.kasource.web.webocket.dropwizard;


import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.annotation.WebSocket;
import org.kasource.web.websocket.config.loader.AnnotatedClientConfigBuilder;
import org.kasource.web.websocket.config.loader.ClientAnnotationConfigurationBuilder;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.servlet.ServletRegistrator;

/**
 * Registers @WebSocket annotated POJOs with the framework.
 * 
 * @author rikardwi
 */
public class WebSocketRegistry implements ServletContextAttributeListener {

  
    private WebSocketListenerRegister register;
    private ServletRegistrator servletRegistrator;
    private volatile boolean isInitialized = false;
    private Queue<Object> waitingWebSocketRegistrations = new LinkedList<Object>();
    private ClientAnnotationConfigurationBuilder configBuilder = new AnnotatedClientConfigBuilder();
    
    public WebSocketRegistry(ClientAnnotationConfigurationBuilder configBuilder) {
       this.configBuilder = configBuilder;
    }
    
    public WebSocketRegistry() {
    }
    
    /**
     * Listen when WebSocketConfig is published, then initialize.
     */
    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        if (event.getValue() instanceof WebSocketConfig) {
            if (!isInitialized) {
                initialize(event);
            }
        }
        
    }
    
    /**
     * On initialize register all pending registrations from the waitingWebSocketRegistrations queue.
     * 
     * @param event
     */
    private synchronized void initialize(ServletContextAttributeEvent event) {
        if (!isInitialized) {
            servletRegistrator = new ServletRegistrator(event.getServletContext(), configBuilder);
            WebSocketConfig config = (WebSocketConfig) event.getValue();
            register = config.getListenerRegister();
            isInitialized = true;
            Object websocketPojo;
            while ((websocketPojo = waitingWebSocketRegistrations.poll()) != null) {
                registerWebSocket(websocketPojo);
            }
        }
    }
    
    /**
     * Register @WebSocket or @WebSocketListener POJOs.
     * 
     * @param websocketPojo POJO to register.
     **/
    public void register(Object websocketPojo) {
        if (!isInitialized) {
            waitingWebSocketRegistrations.add(websocketPojo);
        } else {
            registerWebSocket(websocketPojo);
        }
    }
    
    private void registerWebSocket(Object websocketPojo) {
        if (websocketPojo.getClass().isAnnotationPresent(WebSocket.class)) {
            servletRegistrator.addServlet(websocketPojo.getClass());
        }
        register.registerListener(websocketPojo);
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
       
        
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
       
        
    }

   

}

package org.kasource.web.websocket.cdi.extension;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.ClientConfigImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;


/**
 * Add beans as listeners automatically.
 * 
 *  
 * @author rikardwi
 **/
public class WebSocketEventExtension implements Extension {
    
    private Set<Object> listenerCandidates = new HashSet<Object>();
    private Set<ClientConfigImpl> servletConfigs = new HashSet<ClientConfigImpl>();
    
    /**
     * Handle all injections
     * 
     * @param pit ProcessInjectionTarget to inspect.
     **/
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void processInjectorTarger(@Observes ProcessInjectionTarget<Object> pit) {
         pit.setInjectionTarget(new RegisterWebSocketEventListenerInjectionTarget(pit.getInjectionTarget(), listenerCandidates, servletConfigs));         
       
    }

    
    /**
    * Eagerly loads the Event Dispatcher.
    * 
    * @param event             AfterBeanDiscovery event.
    * @param eventDispatcher   Event Dispatcher to eagerly load.
    */
   void afterAfterDeploymentValidation(@Observes AfterDeploymentValidation event, WebSocketListenerRegister listenerRegister, WebSocketConfig config) {
       for(Object listenerCandidate : listenerCandidates) {
           listenerRegister.registerListener(listenerCandidate);
       }
       for(ClientConfigImpl servletConfig : servletConfigs) {
           if(servletConfig.getServletName() != null) {
               config.registerClientConfig(servletConfig);
           }
       }
   }

   
    

    
}

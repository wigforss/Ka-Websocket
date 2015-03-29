package org.kasource.web.websocket.spring.config.springns.support;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringWebSocketConfigFactoryBean  implements FactoryBean<WebSocketConfig>, ApplicationContextAware {
  
    
    private ProtocolRepository protocolRepository; 
    private WebSocketManagerRepository managerRepository;
    private WebSocketChannelFactory channelFactory;
    private WebSocketListenerRegister listenerRegister;
    private ApplicationContext applicationContext;
    private ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();
    
    @Override
    public WebSocketConfig getObject() throws Exception {
        OriginWhiteListConfig originList =  null;
        try {
             originList = applicationContext.getBean(KaWebSocketBean.ORIGIN_WHITELIST_ID, OriginWhiteListConfig.class);
        } catch (BeansException e) {
        }
        
        AuthenticationConfig authenticationConfig = null;     
        try {
            authenticationConfig = applicationContext.getBean(KaWebSocketBean.AUTHENTICATION_CONFIG_ID, AuthenticationConfig.class);
        }catch (BeansException e) {
        }
        
        
        WebSocketConfigImpl config = new WebSocketConfigImpl();
     
        config.setClientIdGenerator(clientIdGenerator);
        config.setListenerRegister(listenerRegister);
        config.setChannelFactory(channelFactory);
        config.setManagerRepository(managerRepository);
        config.setProtocolRepository(protocolRepository);
        if (originList != null && originList.getOriginWhiteList() != null) {
            config.setOriginWhitelist(originList.getOriginWhiteList());
        }
        if (authenticationConfig != null) {
            config.setAuthenticationProvider(authenticationConfig.getAuthenticationProvider());
        }
       
       
        return config;
    }

    
   
    
    @Override
    public Class<?> getObjectType() {
       
        return WebSocketConfigImpl.class;
    }

    @Override
    public boolean isSingleton() {
   
        return true;
    }

    
    /**
     * @param protocolHandlerRepository the protocolHandlerRepository to set
     */
    @Required
    public void setProtocolRepository(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    /**
     * @param managerRepository the managerRepository to set
     */
    @Required
    public void setManagerRepository(WebSocketManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * @param channelFactory the channelFactory to set
     */
    @Required
    public void setChannelFactory(WebSocketChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }




    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        
    }

    /**
     * @param clientIdGenerator the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }


    /**
     * @param listenerRegister the listenerRegister to set
     */
    public void setListenerRegister(WebSocketListenerRegister listenerRegister) {
        this.listenerRegister = listenerRegister;
    }

}

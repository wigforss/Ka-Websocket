package org.kasource.web.websocket.spring.config.annotation;



import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.channel.client.ClientChannelRepositoryImpl;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.channel.server.ServerChannelFactoryImpl;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;
import org.kasource.web.websocket.spring.bootstrap.SpringWebSocketBootstrap;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.kasource.web.websocket.spring.config.loader.SpringWebSocketServletConfigBuilder;
import org.kasource.web.websocket.spring.registration.WebSocketListenerPostBeanProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.context.ServletContextAware;

@Configuration
public class SpringKaWebSocket implements ServletContextAware {
    
    private ServletContext servletContext;
    
   
    @Autowired
    private ApplicationContext applicationContext;
    

    @Autowired
    @Bean(name = KaWebSocketBean.BOOTSTRAP_ID)
    public SpringWebSocketBootstrap getConfigurer(WebSocketConfig config, 
                                                  ServerChannelFactory channelFactory) {
        SpringWebSocketBootstrap configurer = new SpringWebSocketBootstrap(config);
        configurer.setServletContext(servletContext);
        configurer.setChannelFactory(channelFactory);
        configurer.configure();
        return configurer;
    }
    
    @SuppressWarnings("unchecked")
    @Bean(name = KaWebSocketBean.PROTOCOL_REPO_ID)
    public ProtocolRepository getProtocolRepository() throws Exception {
        ProtocolHandlerConfig<String> textProtocolHandlerConfig = null;
        ProtocolHandlerConfig<byte[]> binaryProtocolHandlerConfig = null;     
        try {
            textProtocolHandlerConfig = applicationContext.getBean(KaWebSocketBean.TEXT_PROTOCOLS_CONFIG_ID, ProtocolHandlerConfig.class);
        } catch (BeansException e) {
        }
        try {
            binaryProtocolHandlerConfig = applicationContext.getBean(KaWebSocketBean.BINARY_PROTOCOLS_CONFIG_ID, ProtocolHandlerConfig.class);
        } catch (BeansException e) {
        }
        
        ProtocolRepositoryImpl handler = new ProtocolRepositoryImpl(textProtocolHandlerConfig, binaryProtocolHandlerConfig);
        return handler;
    }
    
    @Bean(name = KaWebSocketBean.MANAGER_REPO_ID)
    public ClientChannelRepository getWebSocketManagerRepository() throws Exception {
        return new ClientChannelRepositoryImpl(servletContext);
        
    }
    
 
    @Bean(name = KaWebSocketBean.CHANNEL_FACTORY_ID)
    public ServerChannelFactory getWebSocketChannelFactory() throws Exception { 
        ServerChannelFactoryImpl channelFactory = new ServerChannelFactoryImpl();
        channelFactory.initialize(servletContext);
        return channelFactory;
    }
    
    @Bean(name = KaWebSocketBean.LISTENER_REGISTER_ID)
    public WebSocketListenerRegister getWebSocketListenerRegister() throws Exception {
        WebSocketListenerRegisterImpl listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
        return listenerRegister;
    }
    
    @Bean(name = KaWebSocketBean.WEBSOCKET_SERVLET_CONFIG_BUILDER_ID)
    public SpringWebSocketServletConfigBuilder configBuilder(ApplicationContext applicationContext) {
        SpringWebSocketServletConfigBuilder configBuilder = new SpringWebSocketServletConfigBuilder();
        configBuilder.setApplicationContext(applicationContext);
        return configBuilder;
    }

    @Autowired
    @Bean(name = KaWebSocketBean.POST_BEAN_PROCESSOR_ID)
    @DependsOn(KaWebSocketBean.BOOTSTRAP_ID)
    public WebSocketListenerPostBeanProcessor getWebSocketListenerPostBeanProcessor(WebSocketListenerRegister register,
                                                                                    SpringWebSocketServletConfigBuilder configBuilder) throws Exception {
        WebSocketListenerPostBeanProcessor processor = new WebSocketListenerPostBeanProcessor();
        processor.setServletContext(servletContext);
        processor.setListenerRegister(register);
        processor.setConfigBuilder(configBuilder);
        processor.afterPropertiesSet();
        return processor;
    }
    
    @Autowired
    @Bean(name = KaWebSocketBean.CONFIG_ID)
    public WebSocketConfig getWebSocketConfig(ServerChannelFactory channelFactory,
                                              WebSocketListenerRegister listenerRegister,
                                              ClientChannelRepository clientChannelRepository,
                                              ProtocolRepository protocolRepository) throws Exception {
        ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();
        try {
            clientIdGenerator = applicationContext.getBean(KaWebSocketBean.CLIENT_ID_GENERATOR_ID, ClientIdGenerator.class);
        } catch (BeansException e) {
            
        }
        OriginWhiteListConfig originList = null;
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
        config.setServerChannelFactory(channelFactory);
        config.setManagerRepository(clientChannelRepository);
        config.setProtocolRepository(protocolRepository);
        if (clientIdGenerator != null) {
            config.setClientIdGenerator(clientIdGenerator);
        }
        if (originList != null && originList.getOriginWhiteList() != null) {
            config.setOriginWhitelist(originList.getOriginWhiteList());
        }
        if (authenticationConfig != null) {
            config.setAuthenticationProvider(authenticationConfig.getAuthenticationProvider());
        }
        return config;
    }
    
   
    
    @Override
    public void setServletContext(ServletContext servletContext) {
       this.servletContext = servletContext;
        
    }

    
    
   
}

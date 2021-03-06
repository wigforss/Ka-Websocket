package org.kasource.web.websocket.cdi.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.kasource.web.websocket.cdi.util.AnnotationUtil;
import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.channel.client.ClientChannelRepositoryImpl;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.EndpointConfigImpl;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;




@WebListener
public class KaWebSocketConfigurer  implements ServletContextAttributeListener, ServletContextListener {
    private ServletContext servletContext;
    
    @Inject
    private BeanManager beanManager;
    
    @Inject
    private WebSocketConfig config;
    
    @Inject
    private ServerChannelFactory channelFactory;
    /*
    @Inject @Any 
    private Event<ServletContext> contextEvent;
    */
    
    
    public void configure() {    
      //  servletContext.addListener(this);
        beanManager.fireEvent(servletContext);
      //  contextEvent.fire(servletContext);
      //  registerServletConfigs();
        servletContext.setAttribute(WebSocketConfig.class.getName(), config);
       
    }
    
    public void registerServletConfigs() {
        Set<EndpointConfigImpl> servletConfigs = getBeansOfType(EndpointConfigImpl.class);
        for(EndpointConfigImpl servletConfig : servletConfigs) {
            if(servletConfig.getName() != null) {
                config.registerEndpointConfig(servletConfig);
            }
        }
    }
    
   
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Produces @ApplicationScoped 
    public ProtocolRepository getProtocolRepository() {
        ProtocolRepositoryImpl handlerRepo = new ProtocolRepositoryImpl();
        ProtocolHandlerConfig<String> textProtocolHandlerConfig = null;
        ProtocolHandlerConfig<byte[]> binaryProtocolHandlerConfig = null;
        Set<ProtocolHandlerConfig> protocolHandlers = getBeansOfType(ProtocolHandlerConfig.class);
       
        for(ProtocolHandlerConfig protocolConfig : protocolHandlers) {
            Type genericType = ((ParameterizedType) protocolConfig.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            if(genericType.equals(String.class)) {
                textProtocolHandlerConfig = protocolConfig;
            } else if(genericType.equals(byte[].class)) {
                binaryProtocolHandlerConfig = protocolConfig;
            }
        }
        handlerRepo.setTextProtocolHandlerConfig(textProtocolHandlerConfig);
        handlerRepo.setBinaryProtocolHandlerConfig(binaryProtocolHandlerConfig);
        return handlerRepo;
    }
    
    @Produces @ApplicationScoped 
    public  ClientChannelRepository getWebSocketManagerRepository(ProtocolRepository protocolHandlerRepository) {
        ClientChannelRepositoryImpl managerRepo = new ClientChannelRepositoryImpl();
       
        managerRepo.setServletContext(servletContext);
        
        return managerRepo;
    }
    
    @Produces @ApplicationScoped 
    public  WebSocketListenerRegister getWebSocketListenerRegister(ServerChannelFactory channelFactory) {
        return new WebSocketListenerRegisterImpl(servletContext);
    }
    
    @Produces @ApplicationScoped 
    public WebSocketConfig getWebSocketConfig(ServerChannelFactory channelFactory,
                ClientChannelRepository managerRepository,
                ProtocolRepository protocolHandlerRepository,
                WebSocketListenerRegister listenerRegister) {
        WebSocketConfigImpl config = new WebSocketConfigImpl();
        
        
        Set<ClientIdGenerator> idGeneratorSet = getBeansOfType(ClientIdGenerator.class);
        if(!idGeneratorSet.isEmpty()) {
            ClientIdGenerator idGenerator = idGeneratorSet.iterator().next();
            config.setClientIdGenerator(idGenerator);
        }
        
        Set<OriginWhiteListConfig> originWhiteListSet = getBeansOfType(OriginWhiteListConfig.class);
        if(!originWhiteListSet.isEmpty()) {
            OriginWhiteListConfig originWhiteListConfig = originWhiteListSet.iterator().next();
            if(originWhiteListConfig.getOriginWhiteList() != null) {
                config.setOriginWhitelist(originWhiteListConfig.getOriginWhiteList());
            }
        }
        Set<AuthenticationConfig> authConfigSet = getBeansOfType(AuthenticationConfig.class);
        if(!authConfigSet.isEmpty()) {
            AuthenticationConfig authConfig = authConfigSet.iterator().next();
            config.setAuthenticationProvider(authConfig.getAuthenticationProvider());
        }
        
        
        config.setServerChannelFactory(channelFactory);
        config.setManagerRepository(managerRepository);
        config.setProtocolRepository(protocolHandlerRepository);
        config.setListenerRegister(listenerRegister);
        return config;
    }
    
    @Produces @Dependent
    public EndpointConfigImpl getWebSocketServletConfig(ClientChannelRepository managerRepository,
                ProtocolRepository protocolHandlerRepository) {
        EndpointConfigImpl servletConfig = new EndpointConfigImpl();
        servletConfig.setManagerRepository(managerRepository);
        servletConfig.setProtocolRepository(protocolHandlerRepository);
      
        return servletConfig;
    }

    
    @SuppressWarnings("unchecked")
    private <T> Set<T> getBeansOfType(Class<T> ofType, Annotation ... qualifiers) {
        Set<T> setOfBeans = new HashSet<T>();
        Set<Bean<?>> beans = beanManager.getBeans(ofType, qualifiers);
        for(Bean<?> bean : beans) {
          
            T object = (T) beanManager.getReference(bean, ofType, beanManager.createCreationalContext(bean));
            setOfBeans.add(object);
        }
        return setOfBeans;
    }
    
   
    
    
    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        channelFactory.addClientChannelFromAttribute(event.getName(), event.getValue());
        
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
       
        
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
       
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        
        
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.servletContext = servletContextEvent.getServletContext();
        configure();
        
    }
    
}

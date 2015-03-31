package org.kasource.web.websocket.guice;



import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.channel.client.ClientChannelRepositoryImpl;
import org.kasource.web.websocket.channel.server.ServerChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.ClientConfigImpl;
import org.kasource.web.websocket.guice.channel.GuiceWebSocketChannelFactory;
import org.kasource.web.websocket.guice.config.GuiceKaWebSocketConfigurer;
import org.kasource.web.websocket.guice.config.loader.GuiceWebSocketServletConfigBuilder;
import org.kasource.web.websocket.guice.extension.InjectionListenerRegister;
import org.kasource.web.websocket.guice.extension.InjectionTypeListener;
import org.kasource.web.websocket.guice.registration.WebSocketListenerInjectionListener;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;
import org.kasource.web.websocket.servlet.ServletRegistrator;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

public class KaWebSocketModule  extends AbstractModule {

    private WebSocketListenerInjectionListener listener;
    private GuiceKaWebSocketConfigurer configurer;
    
    @Override
    protected void configure() {
        listener = new WebSocketListenerInjectionListener();
        configurer = new GuiceKaWebSocketConfigurer();
        
        InjectionTypeListener typeListener = new InjectionTypeListener(getInjectionListenerRegister(listener));
        bind(ServerChannelFactory.class).to(GuiceWebSocketChannelFactory.class);
        bindListener(Matchers.any(), typeListener); 
       
        requestInjection(listener);
        requestInjection(configurer);
    }
    
 
    /**
     * Returns the InjectionListenerRegister to use.
     * 
     * @param webSocketListener The websocket listener register.
     * 
     * @return a new InjectionListenerRegister.
     **/
    protected InjectionListenerRegister getInjectionListenerRegister(WebSocketListenerInjectionListener webSocketListenerRegister) {
        InjectionListenerRegister register = new InjectionListenerRegister();
        register.addListener(webSocketListenerRegister);
        return register;
    }
    
    @Provides @Singleton
    ProtocolRepository getProtocolHandlerRepository(Injector injector) {
        ProtocolHandlerConfig<String> textHandler = null;
        ProtocolHandlerConfig<byte[]> binaryHandler = null;
        try {
            textHandler = injector.getInstance(TextProtocolHandlerConfigImpl.class);
        }catch (Exception e) {
            // ignore
        }
        try {
            binaryHandler = injector.getInstance(BinaryProtocolHandlerConfigImpl.class);
        }catch (Exception e) {
            // ignore
        }
        ProtocolRepositoryImpl handler = new ProtocolRepositoryImpl();
        handler.setBinaryProtocolHandlerConfig(binaryHandler);
        handler.setTextProtocolHandlerConfig(textHandler);
        return handler;
    }
    
    @Provides @Singleton
    ClientChannelRepository getWebSocketManagerRepository(Injector injector, ProtocolRepository protocolHandlerRepository) {
        ServletContext servletContext = injector.getInstance(ServletContext.class);
       
        
        ClientChannelRepositoryImpl managerRepo = new ClientChannelRepositoryImpl();
        managerRepo.setServletContext(servletContext);
       
        return managerRepo;
    }
    
    @Provides @Singleton
    WebSocketListenerRegister getWebSocketListenerRegister(Injector injector, ServerChannelFactory channelFactory) {
        ServletContext servletContext = injector.getInstance(ServletContext.class);
        return new WebSocketListenerRegisterImpl(servletContext);
    }
    
    @Provides @Singleton 
    WebSocketConfig getWebSocketConfig(ServerChannelFactory channelFactory,
                ClientChannelRepository clientChannelRepository,
                ProtocolRepository protocolHandlerRepository,
                WebSocketListenerRegister listenerRegister,
                Injector injector) {
        WebSocketConfigImpl config = new WebSocketConfigImpl();
        config.setServerChannelFactory(channelFactory);
        config.setManagerRepository(clientChannelRepository);
        config.setProtocolRepository(protocolHandlerRepository);
        config.setListenerRegister(listenerRegister);
        try {
            ClientIdGenerator idGeneraror = injector.getInstance(ClientIdGenerator.class);
            config.setClientIdGenerator(idGeneraror);
        }catch (Exception e) {
            // Ignore
        }
        try {
            OriginWhiteListConfig originWhiteListConfig = injector.getInstance(OriginWhiteListConfig.class);
            config.setOriginWhitelist(originWhiteListConfig.getOriginWhiteList());
            
        } catch (Exception e) {
            
        }
        AuthenticationConfig authConfig = null;
        try {
            authConfig = injector.getInstance(AuthenticationConfig.class);
        }catch (Exception e) {
            // Ingore
        }
        if (authConfig != null) {
            config.setAuthenticationProvider(authConfig.getAuthenticationProvider());
        }
        return config;
    }
    
    
    @Provides @Singleton
    ServletRegistrator getServletRegistrator(Injector injector, GuiceWebSocketServletConfigBuilder configurationBuilder) {
        ServletContext servletContext = injector.getInstance(ServletContext.class);
        return new ServletRegistrator(servletContext, configurationBuilder);
    }
}

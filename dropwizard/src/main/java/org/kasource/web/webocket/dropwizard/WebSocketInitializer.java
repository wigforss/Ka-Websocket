package org.kasource.web.webocket.dropwizard;

import io.dropwizard.Configuration;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.setup.Environment;

import java.lang.reflect.Field;

import org.kasource.web.webocket.dropwizard.auth.BasicAuthenticationProvider;
import org.kasource.web.webocket.dropwizard.auth.OAuth2AuthenticationProvider;
import org.kasource.web.webocket.dropwizard.config.WebSocketConfiguration;
import org.kasource.web.webocket.dropwizard.util.ConfigLoader;
import org.kasource.web.websocket.bootstrap.WebSocketBootstrap;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.loader.ClientAnnotationConfigurationBuilder;

/**
 * Initializes Ka Websocket framework and allows registration of @WebSocket annotated POJOs.
 *
 * @param <T> Application Configuration
 * 
 * @author rikardwi
 */
public class WebSocketInitializer<T extends Configuration> {

    private final T configuration;
    private final Environment environment;
    private final Authenticator<BasicCredentials, ?> basicAuthenticator;  
    private final Authenticator<String, ?> oAuthAuthenticator; 
    private final WebSocketRegistry registry;
    
    private WebSocketInitializer(Builder<T> builder) {
        configuration = builder.configuration;
        environment = builder.environment;
        basicAuthenticator = builder.basicAuthenticator;
        oAuthAuthenticator = builder.oAuthAuthenticator;
        registry = builder.registry;
    }
    
    public static class Builder<T extends Configuration> {
        private T configuration;
        private Environment environment;
        private Authenticator<BasicCredentials, ?> basicAuthenticator;
        private Authenticator<String, ?> oAuthAuthenticator;
        private ClientAnnotationConfigurationBuilder configBuilder;
        private WebSocketRegistry registry;
        
        public Builder(T configuration, 
                      Environment environment) {
            this.configuration = configuration;
            this.environment = environment;
        }
        
        public WebSocketInitializer<T> build() {
            if (configBuilder == null) {
                registry = new WebSocketRegistry();
            } else {
                registry = new WebSocketRegistry(configBuilder);
            }
            return new WebSocketInitializer<T>(this);
        }
        
        public Builder<T> basicAuthenticator(Authenticator<BasicCredentials, ?> authenticator) {
            this.basicAuthenticator = authenticator;
            return this;
        }
        
        public Builder<T> oAuthAuthenticator(Authenticator<String, ?> authenticator) {
            this.oAuthAuthenticator = authenticator;
            return this;
        }
        
        /**
         * Configuration builder for @Websocket annotated classes.
         * <p/> 
         * Handles annotations such as @Authentition, @ClientId, @DefaultBinaryProtocol, @BinaryProtocol
         * @DefaultTextProtocol and @TextProtocol.
         * <p/>
         * The default looks up references by creating new instances with empty public constructor.
         * <p/>
         * If dependency injection is used, a DI specific implementation can be provided (Spring, Guice etc).
         * 
         * @param configBuilder The Configuration builder to use when looking up annotated resources.
         * 
         * @return This builder to allow chaining.
         **/
        public Builder<T> configBuilder(ClientAnnotationConfigurationBuilder configBuilder) {
            this.configBuilder = configBuilder;
            return this;
        }
        
    }
    
    /**
     * Invoke this method in your application run method to enable websockets.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onRun() {
        WebSocketConfiguration ymlConfiguration = getWebSocketConfiguration(configuration);       
        
       
        WebSocketConfigImpl theConfig = new ConfigLoader().configure(ymlConfiguration);
        if (basicAuthenticator != null) {
            BasicAuthenticationProvider provider = new BasicAuthenticationProvider(basicAuthenticator);
            theConfig.setAuthenticationProvider(provider);
        } else if (oAuthAuthenticator != null) {
            OAuth2AuthenticationProvider provider = new OAuth2AuthenticationProvider(oAuthAuthenticator);
            theConfig.setAuthenticationProvider(provider);
        }
        
        environment.servlets().addServletListeners(new WebSocketBootstrap(theConfig));
        environment.servlets().addServletListeners(registry);
        environment.getApplicationContext().addBean(this);
    }
   
    
    private WebSocketConfiguration getWebSocketConfiguration(T configuration) {
        Field[] fields = configuration.getClass().getDeclaredFields();
        for (Field field :fields) {
            field.setAccessible(true);
            if (WebSocketConfiguration.class.equals(field.getType())) {
                try {
                    return (WebSocketConfiguration) field.get(configuration);
                } catch (Exception e) {
                  
                }
            }
        }
        return null;
    }
    
    /**
     * Add a @Websocket annotated POJO.
     * 
     * @param webSocketPojo The @Websocket annotated POJO to register. 
     */
    public void addWebocket(Object webSocketPojo) {
        registry.register(webSocketPojo);
    }

}

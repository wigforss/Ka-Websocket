package org.kasource.web.websocket.guice.example.web;

import java.util.HashSet;
import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.ClientIdGeneratorImpl;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.guice.KaWebSocketModule;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class ExampleModule extends AbstractModule {

    @Override
    protected void configure() {
       KaWebSocketModule websocket = new KaWebSocketModule();
       install(websocket);      
    }
    
  
   @Provides
   AuthenticationProvider getAuthenticationProvider() {
       return new PassthroughAutenticationProvider();
   }
  
   @Provides @Named("jsonHandler")
   TextProtocolHandler getTextProtocolHandler() {
       return new JsonProtocolHandler();
   }
   
   @Provides
   ClientIdGenerator getClientIdGenerator() {
       return new ClientIdGeneratorImpl();
   }
   
  
   
   @Provides @Singleton
   OriginWhiteListConfig getOriginWhiteListConfig() {
       OriginWhiteListConfig list = new OriginWhiteListConfig();
       Set<String> origins = new HashSet<String>();
       origins.add("http://localhost:8080");
       origins.add("localhost:8080");
       list.setOriginWhiteList(origins);
       return list;
   }
   
  
   
   
}

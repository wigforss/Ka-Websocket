package org.kasource.web.websocket.config;

import java.util.Set;

import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.WebSocketClientBuilderFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.security.AuthenticationProvider;

public interface EndpointConfig {
    boolean isDynamicAddressing();

    WebSocketClientBuilderFactory getClientBuilder(ClientChannel manager);

    ClientChannel getClientChannelFor(String url);

    AuthenticationProvider getAuthenticationProvider();
    
    ProtocolRepository getProtocolRepository();

    Set<String> getOriginWhitelist();
    
    boolean isValidOrigin(String origin);
    
    String getName();
    
    ClientIdGenerator getClientIdGenerator();
    
    String getUrl();
    
    long getAsyncSendTimeoutMillis();
   
    int getMaxBinaryMessageBufferSizeByte();
    
    long getMaxSessionIdleTimeoutMillis();

    int getMaxTextMessageBufferSizeByte();


}

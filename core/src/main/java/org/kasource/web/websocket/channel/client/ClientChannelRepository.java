package org.kasource.web.websocket.channel.client;


public interface ClientChannelRepository {
    public static final String ATTRIBUTE_PREFIX = ClientChannel.class.getName() + "_";
        
    
    public ClientChannel getClientChannel(String url);
    
}

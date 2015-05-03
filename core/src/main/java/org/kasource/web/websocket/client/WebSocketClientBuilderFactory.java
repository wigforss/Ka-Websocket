package org.kasource.web.websocket.client;


import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.EndpointConfig;

public class WebSocketClientBuilderFactory {
    private EndpointConfig endpointConfig;
    private ClientChannel manager;
    private ClientIdGenerator clientIdGenerator;
    
    public WebSocketClientBuilderFactory(EndpointConfig endpointConfig, ClientChannel manager, ClientIdGenerator clientIdGenerator) {
        this.endpointConfig = endpointConfig;
        this.manager = manager;
        this.clientIdGenerator = clientIdGenerator;
    }
    
    public WebSocketClientConfig.Builder get(HandshakeRequest request) {
        String clientId = clientIdGenerator.getId(request, manager);
        return new WebSocketClientConfig.Builder(endpointConfig, manager, clientId, HandshakeRequestData.fromRequest(request));
    }
}

package org.kasource.web.websocket.client;


import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.client.id.ClientIdGenerator;

public class WebSocketClientBuilderFactory {
    private ClientChannel manager;
    private ClientIdGenerator clientIdGenerator;
    
    public WebSocketClientBuilderFactory(ClientChannel manager, ClientIdGenerator clientIdGenerator) {
        this.manager = manager;
        this.clientIdGenerator = clientIdGenerator;
    }
    
    public WebSocketClientConfig.Builder get(HttpServletRequest request) {
        String clientId = clientIdGenerator.getId(request, manager);
        return new WebSocketClientConfig.Builder(manager, clientId, UpgradeRequestData.fromRequest(request));
    }
}

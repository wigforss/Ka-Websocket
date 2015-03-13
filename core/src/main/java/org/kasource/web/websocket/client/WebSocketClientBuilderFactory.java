package org.kasource.web.websocket.client;


import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManager;

public class WebSocketClientBuilderFactory {
    private WebSocketManager manager;
    private ClientIdGenerator clientIdGenerator;
    
    public WebSocketClientBuilderFactory(WebSocketManager manager, ClientIdGenerator clientIdGenerator) {
        this.manager = manager;
        this.clientIdGenerator = clientIdGenerator;
    }
    
    public WebSocketClientConfig.Builder get(HttpServletRequest request) {
        String clientId = clientIdGenerator.getId(request, manager);
        return new WebSocketClientConfig.Builder(manager, clientId, request.getParameterMap());
    }
}

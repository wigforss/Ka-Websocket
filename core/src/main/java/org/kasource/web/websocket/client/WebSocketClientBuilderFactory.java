package org.kasource.web.websocket.client;

import java.util.Map;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.util.HeaderLookup;

public class WebSocketClientBuilderFactory {
    private WebSocketManager manager;
    private ClientIdGenerator clientIdGenerator;
    
    public WebSocketClientBuilderFactory(WebSocketManager manager, ClientIdGenerator clientIdGenerator) {
        this.manager = manager;
        this.clientIdGenerator = clientIdGenerator;
    }
    
    public WebSocketClientConfig.Builder get(Map<String, String[]> requestParameters, HeaderLookup headers) {
        String clientId = clientIdGenerator.getId(requestParameters, headers, manager);
        return new WebSocketClientConfig.Builder(manager, clientId, requestParameters);
    }
}

package org.kasource.web.websocket.config;

import org.kasource.web.websocket.client.WebSocketClientBuilderFactory;
import org.kasource.web.websocket.manager.WebSocketManager;

public interface WebSocketServletConfig {
    boolean isDynamicAddressing();

    WebSocketClientBuilderFactory getClientBuilder(WebSocketManager manager);

    WebSocketManager getWebSocketManager(String url);

    boolean hasProtocol(String protocol, String url);

    boolean isValidOrigin(String origin);

    boolean hasClientIdGenerator();
}

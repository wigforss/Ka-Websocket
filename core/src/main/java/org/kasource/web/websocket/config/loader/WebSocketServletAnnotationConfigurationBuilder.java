package org.kasource.web.websocket.config.loader;

import org.kasource.web.websocket.config.WebSocketServletConfigImpl;

public interface WebSocketServletAnnotationConfigurationBuilder {
    public WebSocketServletConfigImpl configure(Class<?> webocketPojo);
}

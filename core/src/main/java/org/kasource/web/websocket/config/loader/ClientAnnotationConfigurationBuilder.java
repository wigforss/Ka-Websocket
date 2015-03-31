package org.kasource.web.websocket.config.loader;

import org.kasource.web.websocket.config.ClientConfigImpl;

public interface ClientAnnotationConfigurationBuilder {
    public ClientConfigImpl configure(Class<?> webocketPojo);
}

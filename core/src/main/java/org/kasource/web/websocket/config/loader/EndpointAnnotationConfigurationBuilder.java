package org.kasource.web.websocket.config.loader;

import org.kasource.web.websocket.config.EndpointConfigImpl;

public interface EndpointAnnotationConfigurationBuilder {
    public EndpointConfigImpl configure(Class<?> webocketPojo);
}

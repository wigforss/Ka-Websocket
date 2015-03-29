package org.kasource.web.websocket.manager;

import java.lang.annotation.Annotation;

import org.kasource.commons.collection.builder.ListBuilder;
import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.commons.reflection.parameter.binder.EnvironmentParameterBinder;
import org.kasource.commons.reflection.parameter.binder.SystemPropertyParameterBinder;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.parameter.ClientHeaderBinder;
import org.kasource.web.websocket.client.parameter.ClientIpBinder;
import org.kasource.web.websocket.client.parameter.ClientRequestParameterBinder;

public class WebSocketClientRegistration {
    private final WebSocketClient client;
    private final ParameterBinder parameterBinder;
    
    public WebSocketClientRegistration(WebSocketClient client) {
        this.client = client;
        parameterBinder = new ParameterBinder(new ListBuilder<AnnotationParameterBinder<? extends Annotation>>()
                    .add(new ClientRequestParameterBinder(client))
                    .add(new ClientHeaderBinder(client))
                    .add(new SystemPropertyParameterBinder())
                    .add(new EnvironmentParameterBinder())
                    .add(new ClientIpBinder(client))
                    .build());
    }

    /**
     * @return the client
     */
    public WebSocketClient getClient() {
        return client;
    }

    /**
     * @return the parameterBinder
     */
    public ParameterBinder getParameterBinder() {
        return parameterBinder;
    }
    
}

package org.kasource.web.websocket.channel.client;

import java.lang.annotation.Annotation;

import org.kasource.commons.collection.builder.ListBuilder;
import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.commons.reflection.parameter.binder.EnvironmentParameterBinder;
import org.kasource.commons.reflection.parameter.binder.SystemPropertyParameterBinder;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.parameter.ClientHeaderBinder;
import org.kasource.web.websocket.client.parameter.ClientIdBinder;
import org.kasource.web.websocket.client.parameter.ClientIpBinder;
import org.kasource.web.websocket.client.parameter.ClientRequestParameterBinder;
import org.kasource.web.websocket.client.parameter.UsernameBinder;

public class ClientRegistration {
    private final WebSocketClient client;
    private final ParameterBinder parameterBinder;
    
    public ClientRegistration(WebSocketClient client) {
        this.client = client;
        parameterBinder = new ParameterBinder(new ListBuilder<AnnotationParameterBinder<? extends Annotation>>()
                    .add(new ClientRequestParameterBinder(client.getUpgradeRequest()))
                    .add(new ClientHeaderBinder(client.getUpgradeRequest()))
                    .add(new SystemPropertyParameterBinder())
                    .add(new EnvironmentParameterBinder())
                    .add(new ClientIpBinder(client.getUpgradeRequest()))
                    .add(new UsernameBinder(client))
                    .add(new ClientIdBinder(client))
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

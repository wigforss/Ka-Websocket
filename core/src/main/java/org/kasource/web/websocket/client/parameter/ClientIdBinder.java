package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.ClientId;
import org.kasource.web.websocket.client.WebSocketClient;

public class ClientIdBinder implements AnnotationParameterBinder<ClientId> {

    private WebSocketClient client;
    
    public ClientIdBinder(WebSocketClient client) {
        this.client = client;
    }
    
    @Override
    public Object bindValue(ClientId annotation) {
       
        return client.getId();
    }

    @Override
    public boolean isStatic() {
        
        return true;
    }

}

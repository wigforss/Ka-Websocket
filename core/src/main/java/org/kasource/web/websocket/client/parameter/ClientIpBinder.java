package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.config.annotation.ClientId;

public class ClientIpBinder implements AnnotationParameterBinder<ClientId>{

    private String ipAddress;
    
    public ClientIpBinder(WebSocketClient webSocketClient) {
        ipAddress = webSocketClient.getUpgradeRequest().getRemoteAddr();
    }
    
    @Override
    public Object bindValue(ClientId annotation) {
        return ipAddress;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

}

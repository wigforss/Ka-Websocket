package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.Header;
import org.kasource.web.websocket.client.WebSocketClient;

public class ClientHeaderBinder implements AnnotationParameterBinder<Header> {

    private WebSocketClient webSocketClient;
    
    public ClientHeaderBinder(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }
    
    @Override
    public Object bindValue(Header annotation) {
       
        String value = webSocketClient.getUpgradeRequest().getHeader(annotation.value());
        if (value != null) {
            return value;
        } else if (!annotation.defaultValue().isEmpty()) {
            return annotation.defaultValue();
        } else {
            return null;
        }
    }

    @Override
    public boolean isStatic() {
        return true;
    }

}

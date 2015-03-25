package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.RequestParameter;
import org.kasource.web.websocket.client.WebSocketClient;


public class ClientRequestParameterBinder implements AnnotationParameterBinder<RequestParameter> {

    private WebSocketClient webSocketClient;
    
    public ClientRequestParameterBinder(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }
    
    
    @Override
    public Object bindValue(RequestParameter annotation) {
        
        String value = webSocketClient.getUpgradeRequest().getParameter(annotation.value());
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

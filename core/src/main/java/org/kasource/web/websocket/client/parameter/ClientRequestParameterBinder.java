package org.kasource.web.websocket.client.parameter;

import java.util.Map;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.RequestParameter;
import org.kasource.web.websocket.client.WebSocketClient;


public class ClientRequestParameterBinder implements AnnotationParameterBinder<RequestParameter> {

    private Map<String, String[]> parameters;
    
    public ClientRequestParameterBinder(WebSocketClient webSocketClient) {
        this.parameters = webSocketClient.getUpgradeRequest().getParameterMap();
    }
    
    
    @Override
    public Object bindValue(RequestParameter annotation) {
        
        String[] value = parameters.get(annotation.value());
        if (value != null) {
            return value[0];
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

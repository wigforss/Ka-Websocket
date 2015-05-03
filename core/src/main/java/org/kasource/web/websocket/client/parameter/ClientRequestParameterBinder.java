package org.kasource.web.websocket.client.parameter;

import java.util.List;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.RequestParameter;
import org.kasource.web.websocket.client.HandshakeRequestData;


public class ClientRequestParameterBinder implements AnnotationParameterBinder<RequestParameter> {

    private HandshakeRequestData handshakeRequestData;
    
    public ClientRequestParameterBinder(HandshakeRequestData handshakeRequestData) {
        this.handshakeRequestData = handshakeRequestData;
    }
    
    
    @Override
    public Object bindValue(RequestParameter annotation) {
        
        List<String> value = handshakeRequestData.getParameters().get(annotation.value());
        if (value != null && !value.isEmpty()) {
            return value.get(0);
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

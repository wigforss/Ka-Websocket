package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.SessionAttribute;
import org.kasource.web.websocket.client.HandshakeRequestData;


public class ClientSessionAttributeBinder implements AnnotationParameterBinder<SessionAttribute>{

    private HandshakeRequestData handshakeRequestData;
    
    public ClientSessionAttributeBinder(HandshakeRequestData handshakeRequestData) {
        this.handshakeRequestData = handshakeRequestData;
    }
    
    @Override
    public Object bindValue(SessionAttribute annotation) {
        return handshakeRequestData.getHttpSession().getAttribute(annotation.value());
    }

    @Override
    public boolean isStatic() {
        return false;
    }

}

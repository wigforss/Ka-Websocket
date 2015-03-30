package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.Username;
import org.kasource.web.websocket.client.WebSocketClient;

public class UsernameBinder implements AnnotationParameterBinder<Username> {
    protected static final String DEFAULT_USER = "Anonymous";
    
    private WebSocketClient client;
    
    public UsernameBinder(WebSocketClient client) {
        this.client = client;
    }

    @Override
    public Object bindValue(Username annotation) {
        String username = client.getUsername();
        if (username == null) {
            return DEFAULT_USER;
        }
        return username;
    }

    @Override
    public boolean isStatic() {
       
        return true;
    }
    
}

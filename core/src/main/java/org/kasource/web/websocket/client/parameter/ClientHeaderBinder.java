package org.kasource.web.websocket.client.parameter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.Header;
import org.kasource.web.websocket.client.WebSocketClient;

public class ClientHeaderBinder implements AnnotationParameterBinder<Header> {

    private Map<String, String> headers = new HashMap<String, String>();
    
    public ClientHeaderBinder(WebSocketClient webSocketClient) {
        Enumeration<String> headerNames = webSocketClient.getUpgradeRequest().getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, webSocketClient.getUpgradeRequest().getHeader(headerName));
            
        }
    }
    
    @Override
    public Object bindValue(Header annotation) {
       
        String value = headers.get(annotation.value());
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

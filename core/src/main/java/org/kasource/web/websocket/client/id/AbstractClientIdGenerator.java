package org.kasource.web.websocket.client.id;


import java.util.List;
import java.util.UUID;

import javax.websocket.server.HandshakeRequest;


public abstract class AbstractClientIdGenerator implements ClientIdGenerator {
    private static final String DEFAULT_ID_KEY = "username";
    
    private String idKey = DEFAULT_ID_KEY;
    private boolean useHeaderValue;
    
    
    /**
     * @param idKey the idKey to set
     */
    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }
    
    /**
     * @param headerValue the headerValue to set
     */
    public void setUseHeaderValue(boolean useHeaderValue) {
        this.useHeaderValue = useHeaderValue;
    }
    
    protected String getIdValue(HandshakeRequest request) {
        if (useHeaderValue) {
            List<String> idHeaders = request.getHeaders().get(idKey);
            if (idHeaders != null && !idHeaders.isEmpty()) {
                return idHeaders.get(0);
            }
        } else {
            List<String> value = request.getParameterMap().get(idKey);
            if (value != null && !value.isEmpty()) {
                return value.get(0);
            } 
        }
        return null;
    }
    
    protected String getUuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

  
}

package org.kasource.web.websocket.client.id;


import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


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
    
    protected String getIdValue(HttpServletRequest request) {
        if (useHeaderValue) {
            return request.getHeader(idKey);
        } else {
            String value = request.getParameter(idKey);
            if (value != null) {
                return value;
            } else {
                return null;
            }
        }
    }
    
    protected String getUuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

  
}

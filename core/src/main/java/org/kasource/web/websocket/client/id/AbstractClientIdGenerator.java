package org.kasource.web.websocket.client.id;

import java.util.Map;
import java.util.UUID;


import org.kasource.web.websocket.util.HeaderLookup;

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
    
    protected String getIdValue(Map<String, String[]> requestParameters, 
                                HeaderLookup headerLookup) {
        if (useHeaderValue) {
            return headerLookup.getHeaderValue(idKey);
        } else {
            String[] values = requestParameters.get(idKey);
            if (values != null && values.length > 0) {
                return values[0];
            } else {
                return null;
            }
        }
    }
    
    protected String getUuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

  
}

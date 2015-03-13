package org.kasource.web.websocket.client.id;




import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.util.HeaderLookup;

/**
 * Generates a client ID.
 * 
 * @author rikardwi
 **/
public class ClientIdGeneratorImpl extends AbstractClientIdGenerator {
   
 
    @Override
    public String getId(Map<String, String[]> requestParameters, 
                        HeaderLookup headerLookup, 
                        WebSocketManager manager) {
        String clientId = getIdValue(requestParameters, headerLookup);
        if (manager.hasClient(clientId)) {
            clientId = clientId + "-" + getUuid();
        }
        if (clientId == null) {
            clientId = getUuid();
        }
        
        return clientId;
    }
    
}

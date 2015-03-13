package org.kasource.web.websocket.client.id;

import java.util.Map;

import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.util.HeaderLookup;

public interface ClientIdGenerator {

    /**
     * Returns a new client ID from a request.
     * 
     * Tries to reuse username as client ID if possible, to allow for
     * easier debugging.
     * 
     * @param requestParameters   The HTTP Request parameters
     * @param manager             The WebSocketManager to query for used IDs.
     * 
     * @return A unique client ID for the manager supplied.
     **/
    public String getId(Map<String, String[]> requestParameters, 
                        HeaderLookup headerLookup, 
                        WebSocketManager manager);

}

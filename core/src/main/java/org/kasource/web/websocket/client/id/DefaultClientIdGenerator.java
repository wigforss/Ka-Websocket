package org.kasource.web.websocket.client.id;

import java.util.Map;

import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.util.HeaderLookup;

public class DefaultClientIdGenerator extends AbstractClientIdGenerator {

    @Override
    public String getId(Map<String, String[]> requestParameters, 
                        HeaderLookup headerLookup, 
                        WebSocketManager manager) {
        return getUuid();
    }

}

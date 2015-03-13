package org.kasource.web.websocket.client.id;



import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.manager.WebSocketManager;

public class DefaultClientIdGenerator extends AbstractClientIdGenerator {

    @Override
    public String getId(HttpServletRequest request, 
                        WebSocketManager manager) {
        return getUuid();
    }

}

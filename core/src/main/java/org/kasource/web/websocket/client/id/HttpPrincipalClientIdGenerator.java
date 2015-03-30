package org.kasource.web.websocket.client.id;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.manager.WebSocketManager;

public class HttpPrincipalClientIdGenerator  extends AbstractClientIdGenerator implements ClientIdGenerator {

    @Override
    public String getId(HttpServletRequest request, WebSocketManager manager) {
        Principal principal = request.getUserPrincipal();
        String clientId = null;
        if (principal != null) {
            clientId = principal.getName();
        }
        if (clientId != null) {
            if (manager.hasClient(clientId)) {
                clientId = clientId + "-" + getUuid();
            }
            return clientId;
        } 
        return getUuid();
    }

}

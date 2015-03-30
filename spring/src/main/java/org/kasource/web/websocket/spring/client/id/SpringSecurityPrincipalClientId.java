package org.kasource.web.websocket.spring.client.id;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.client.id.AbstractClientIdGenerator;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityPrincipalClientId extends AbstractClientIdGenerator implements ClientIdGenerator {

    @Override
    public String getId(HttpServletRequest request, WebSocketManager manager) {
        String clientId = getUsername();
        if (clientId == null) {
            return getUuid();
        }
        if (manager.hasClient(clientId)) {
            return clientId + "-" + getUuid();
        } else {
            return clientId;
        }
    }
    
    private String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
           return null;   
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;       
        }
        return authentication.getName();
    }

}

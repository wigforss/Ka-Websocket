package org.kasource.web.websocket.client.id;

import java.security.Principal;

import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.channel.client.ClientChannel;

public class HttpPrincipalClientIdGenerator  extends AbstractClientIdGenerator implements ClientIdGenerator {

    @Override
    public String getId(HandshakeRequest request, ClientChannel manager) {
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

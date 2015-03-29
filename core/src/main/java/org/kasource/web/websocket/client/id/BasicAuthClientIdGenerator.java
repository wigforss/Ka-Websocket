package org.kasource.web.websocket.client.id;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.kasource.web.websocket.manager.WebSocketManager;



public class BasicAuthClientIdGenerator extends AbstractClientIdGenerator implements ClientIdGenerator {
    private static final String BASIC_AUTH_PREFIX = "Basic ";
    @Override
    public String getId(HttpServletRequest request, WebSocketManager manager) {
        String clientId = getUsername(request);
        if (clientId == null) {
            return getUuid();
        }
        if (manager.hasClient(clientId)) {
            return clientId + "-" + getUuid();
        }
        return clientId;
    }
    
    private String getUsername(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(BASIC_AUTH_PREFIX)) {
            String credentials = new String(Base64.decodeBase64(authHeader.substring(BASIC_AUTH_PREFIX.length())), Charset.forName("UTF-8"));
            int index = credentials.indexOf(':');
            return credentials.substring(0, index);
            
        }
        return null;
    }

}

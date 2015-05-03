package org.kasource.web.websocket.client.id;

import java.nio.charset.Charset;
import java.util.List;

import javax.websocket.server.HandshakeRequest;

import org.apache.commons.codec.binary.Base64;
import org.kasource.web.websocket.channel.client.ClientChannel;



public class BasicAuthClientIdGenerator extends AbstractClientIdGenerator implements ClientIdGenerator {
    private static final String BASIC_AUTH_PREFIX = "Basic ";
    @Override
    public String getId(HandshakeRequest request, ClientChannel manager) {
        String clientId = getUsername(request);
        if (clientId == null) {
            return getUuid();
        }
        if (manager.hasClient(clientId)) {
            return clientId + "-" + getUuid();
        }
        return clientId;
    }
    
    private String getUsername(HandshakeRequest request) {
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty() && authHeaders.get(0).startsWith(BASIC_AUTH_PREFIX)) {
            String credentials = new String(Base64.decodeBase64(authHeaders.get(0).substring(BASIC_AUTH_PREFIX.length())), Charset.forName("UTF-8"));
            int index = credentials.indexOf(':');
            return credentials.substring(0, index);
            
        }
        return null;
    }

}

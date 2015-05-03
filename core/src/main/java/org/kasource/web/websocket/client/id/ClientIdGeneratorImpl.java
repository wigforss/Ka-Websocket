package org.kasource.web.websocket.client.id;



import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.channel.client.ClientChannel;

/**
 * Generates a client ID.
 * 
 * @author rikardwi
 **/
public class ClientIdGeneratorImpl extends AbstractClientIdGenerator {
   
 
    @Override
    public String getId(HandshakeRequest request,
                        ClientChannel manager) {
        String clientId = getIdValue(request);
       
        if (manager.hasClient(clientId)) {
            clientId = clientId + "-" + getUuid();
        }
        if (clientId == null) {
            clientId = getUuid();
        }
        
       
        
        return clientId;
    }
    
}

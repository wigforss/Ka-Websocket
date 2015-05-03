package org.kasource.web.websocket.client.id;



import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.channel.client.ClientChannel;

public class DefaultClientIdGenerator extends AbstractClientIdGenerator {

    @Override
    public String getId(HandshakeRequest request, 
                        ClientChannel manager) {
        return getUuid();
    }

}

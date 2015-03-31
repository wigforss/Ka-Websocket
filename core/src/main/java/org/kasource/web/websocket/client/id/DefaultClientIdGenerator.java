package org.kasource.web.websocket.client.id;



import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.channel.client.ClientChannel;

public class DefaultClientIdGenerator extends AbstractClientIdGenerator {

    @Override
    public String getId(HttpServletRequest request, 
                        ClientChannel manager) {
        return getUuid();
    }

}

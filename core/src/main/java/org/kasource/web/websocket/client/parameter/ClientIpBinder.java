package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.client.UpgradeRequestData;
import org.kasource.web.websocket.config.annotation.ClientId;

public class ClientIpBinder implements AnnotationParameterBinder<ClientId>{

    private UpgradeRequestData upgradeRequestData;
    
    public ClientIpBinder(UpgradeRequestData upgradeRequestData) {
        this.upgradeRequestData = upgradeRequestData;
    }
    
    @Override
    public Object bindValue(ClientId annotation) {
        return upgradeRequestData.getIpAddress();
    }

    @Override
    public boolean isStatic() {
        return true;
    }

}

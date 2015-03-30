package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.client.UpgradeRequestData;
import org.kasource.web.websocket.config.annotation.GenerateId;

public class ClientIpBinder implements AnnotationParameterBinder<GenerateId>{

    private UpgradeRequestData upgradeRequestData;
    
    public ClientIpBinder(UpgradeRequestData upgradeRequestData) {
        this.upgradeRequestData = upgradeRequestData;
    }
    
    @Override
    public Object bindValue(GenerateId annotation) {
        return upgradeRequestData.getIpAddress();
    }

    @Override
    public boolean isStatic() {
        return true;
    }

}

package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.RequestParameter;
import org.kasource.web.websocket.client.UpgradeRequestData;


public class ClientRequestParameterBinder implements AnnotationParameterBinder<RequestParameter> {

    private UpgradeRequestData upgradeRequestData;
    
    public ClientRequestParameterBinder(UpgradeRequestData upgradeRequestData) {
        this.upgradeRequestData = upgradeRequestData;
    }
    
    
    @Override
    public Object bindValue(RequestParameter annotation) {
        
        String[] value = upgradeRequestData.getParameters().get(annotation.value());
        if (value != null) {
            return value[0];
        } else if (!annotation.defaultValue().isEmpty()) {
            return annotation.defaultValue();
        } else {
            return null;
        }
    }

    @Override
    public boolean isStatic() {
        return true;
    }

}

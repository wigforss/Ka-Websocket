package org.kasource.web.websocket.client.parameter;

import org.kasource.commons.reflection.parameter.binder.AnnotationParameterBinder;
import org.kasource.web.websocket.annotations.Header;
import org.kasource.web.websocket.client.UpgradeRequestData;

public class ClientHeaderBinder implements AnnotationParameterBinder<Header> {

    private UpgradeRequestData upgradeRequestData;
    
    public ClientHeaderBinder(UpgradeRequestData upgradeRequestData) {
        this.upgradeRequestData = upgradeRequestData;
    }
    
    @Override
    public Object bindValue(Header annotation) {
       
        String value = upgradeRequestData.getHeaders().get(annotation.value());
        if (value != null) {
            return value;
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

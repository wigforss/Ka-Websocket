package org.kasource.web.websocket.config.xml;

import org.kasource.web.websocket.client.id.AbstractClientIdGenerator;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.WebSocketConfigException;

public class XmlClientIdGenerator {
   
    private ClientIdGenerator clientIdGenerator;
    
    public XmlClientIdGenerator(org.kasource.web.websocket.config.xml.jaxb.ClientIdGenerator generatorConfig) {
        try {
            this.clientIdGenerator = loadClientIdGenerator(generatorConfig);
        } catch (Exception e) {
            throw new WebSocketConfigException("Could not load ClientIdGenerator: " + generatorConfig.getClass(), e);
        }
    }
    
    private ClientIdGenerator loadClientIdGenerator(org.kasource.web.websocket.config.xml.jaxb.ClientIdGenerator generatorConfig) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        if (generatorConfig != null) {
            Class<?> clazz = Class.forName(generatorConfig.getClazz());
            ClientIdGenerator generator = (ClientIdGenerator) clazz.newInstance();
            if (generator instanceof AbstractClientIdGenerator) {
                AbstractClientIdGenerator abstractGenerator = (AbstractClientIdGenerator) generator;
                abstractGenerator.setUseHeaderValue(generatorConfig.isHeaderValue());
                if (generatorConfig.getIdKey() != null) {
                    abstractGenerator.setIdKey(generatorConfig.getIdKey());
                }
                
            }
            return generator;
        } else {
            return null;
        }
    }

    /**
     * @return the clientIdGenerator
     */
    public ClientIdGenerator getClientIdGenerator() {
        return clientIdGenerator;
    }
}

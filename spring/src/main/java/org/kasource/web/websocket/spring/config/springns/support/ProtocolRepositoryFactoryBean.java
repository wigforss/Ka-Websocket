package org.kasource.web.websocket.spring.config.springns.support;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.kasource.web.websocket.protocol.ProtocolRepositoryImpl;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ProtocolRepositoryFactoryBean implements FactoryBean<ProtocolRepository>, ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @SuppressWarnings({ "unchecked" })
    @Override
    public ProtocolRepository getObject() throws Exception {
        
        ProtocolHandlerConfig<String> textProtocolHandlerConfig = null;
        ProtocolHandlerConfig<byte[]> binaryProtocolHandlerConfig = null;
        
        try {
            textProtocolHandlerConfig = applicationContext.getBean(KaWebSocketBean.TEXT_PROTOCOLS_CONFIG_ID, ProtocolHandlerConfig.class);
        } catch (BeansException e) {
        }
        try {
            binaryProtocolHandlerConfig = applicationContext.getBean(KaWebSocketBean.BINARY_PROTOCOLS_CONFIG_ID, ProtocolHandlerConfig.class);
        } catch (BeansException e) {
        }
        
        ProtocolRepositoryImpl handler = new ProtocolRepositoryImpl(textProtocolHandlerConfig, binaryProtocolHandlerConfig);
        return handler;
    }

    @Override
    public Class<?> getObjectType() {
        return ProtocolRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        
    }

}

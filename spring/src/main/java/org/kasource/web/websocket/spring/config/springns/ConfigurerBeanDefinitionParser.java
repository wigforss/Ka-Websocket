package org.kasource.web.websocket.spring.config.springns;


import org.kasource.web.websocket.spring.bootstrap.SpringWebSocketBootstrap;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.kasource.web.websocket.spring.config.loader.SpringWebSocketServletConfigBuilder;
import org.kasource.web.websocket.spring.config.springns.support.ProtocolRepositoryFactoryBean;
import org.kasource.web.websocket.spring.config.springns.support.SpringServerChannelFactory;
import org.kasource.web.websocket.spring.config.springns.support.SpringWebSocketConfigFactoryBean;
import org.kasource.web.websocket.spring.config.springns.support.SpringWebSocketListenerRegister;
import org.kasource.web.websocket.spring.config.springns.support.WebSocketManagerRepositoryFactoryBean;
import org.kasource.web.websocket.spring.registration.WebSocketListenerPostBeanProcessor;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ConfigurerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
 
    /**
     * Returns SpringWebSocketConfigurer.
     * 
     * @param element websocket XML element.
     * 
     * @return SpringWebSocketConfigurer class.
     **/
    protected Class<?> getBeanClass(Element element) {
        return SpringWebSocketBootstrap.class;
    }
    
    /**
     * parse the websocket XML element.
     * 
     * @param element websocket XML element.
     * @param pc      Parser context.
     * @param bean    Bean definition.
     **/
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        element.setAttribute(ID_ATTRIBUTE, KaWebSocketBean.BOOTSTRAP_ID);
        element.setAttribute(NAME_ATTRIBUTE, KaWebSocketBean.BOOTSTRAP_ID);
        bean.addPropertyReference("channelFactory", KaWebSocketBean.CHANNEL_FACTORY_ID);
        bean.setInitMethodName("configure");
        bean.addConstructorArgReference(KaWebSocketBean.CONFIG_ID);
        bean.setLazyInit(false);
      
        createBeans(pc, element);

    }
    
    /**
     * Create beans needed by Ka-websocket.
     * 
     * @param pc           Parser Context.
     **/
    private void createBeans(ParserContext pc, Element element) {
        createProtocolRepository(pc);
        createManagerRepository(pc);
        createChannelFactory(pc);
        createListenerRegister(pc);
        createwebsocketConfigBuilder(pc);
        createPostBeanProcessor(pc);
        createConfig(pc, element);
    }
    
    private void createwebsocketConfigBuilder(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                    .rootBeanDefinition(
                     SpringWebSocketServletConfigBuilder.class);
        builder.setLazyInit(false);
        
        pc.registerBeanComponent(new BeanComponentDefinition(builder
                    .getBeanDefinition(), KaWebSocketBean.WEBSOCKET_SERVLET_CONFIG_BUILDER_ID));
    }
    
    private void createListenerRegister(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                    .rootBeanDefinition(
                    SpringWebSocketListenerRegister.class);
        builder.setLazyInit(false);
        
        pc.registerBeanComponent(new BeanComponentDefinition(builder
                    .getBeanDefinition(), KaWebSocketBean.LISTENER_REGISTER_ID));
    }
    
    private void createProtocolRepository(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .rootBeanDefinition(
                            ProtocolRepositoryFactoryBean.class);
        builder.setLazyInit(false);
        
        
        pc.registerBeanComponent(new BeanComponentDefinition(builder
                    .getBeanDefinition(), KaWebSocketBean.PROTOCOL_REPO_ID));
    }
    
    private void createManagerRepository(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .rootBeanDefinition(WebSocketManagerRepositoryFactoryBean.class);
       
        builder.setLazyInit(false);
        pc.registerBeanComponent(new BeanComponentDefinition(builder
            .getBeanDefinition(), KaWebSocketBean.MANAGER_REPO_ID));
    }
    
    private void createConfig(ParserContext pc, Element element) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .rootBeanDefinition(SpringWebSocketConfigFactoryBean.class);
        builder.addPropertyReference("channelFactory", KaWebSocketBean.CHANNEL_FACTORY_ID);
        builder.addPropertyReference("managerRepository", KaWebSocketBean.MANAGER_REPO_ID);
        builder.addPropertyReference("protocolRepository", KaWebSocketBean.PROTOCOL_REPO_ID);
        builder.addPropertyReference("listenerRegister", KaWebSocketBean.LISTENER_REGISTER_ID);
        builder.setLazyInit(false);
        
       
        if (element.getAttribute("clientIdGeneratorRef") != null) {
             String ref = element.getAttribute("clientIdGeneratorRef");
             if(ref != null && !ref.trim().isEmpty()) {
                 builder.addPropertyReference("clientIdGenerator", ref);
             }
        }
            
        
        
        pc.registerBeanComponent(new BeanComponentDefinition(builder
            .getBeanDefinition(), KaWebSocketBean.CONFIG_ID));
    }
    
    private void createChannelFactory(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                    .rootBeanDefinition(SpringServerChannelFactory.class);
        builder.setLazyInit(false);
        pc.registerBeanComponent(new BeanComponentDefinition(builder
            .getBeanDefinition(), KaWebSocketBean.CHANNEL_FACTORY_ID));
    }
    
    private void createPostBeanProcessor(ParserContext pc) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .rootBeanDefinition(WebSocketListenerPostBeanProcessor.class);
        builder.setLazyInit(false);
        builder.addDependsOn(KaWebSocketBean.BOOTSTRAP_ID);
        builder.addPropertyReference("listenerRegister", KaWebSocketBean.LISTENER_REGISTER_ID);
        builder.addPropertyReference("configBuilder", KaWebSocketBean.WEBSOCKET_SERVLET_CONFIG_BUILDER_ID);
        pc.registerBeanComponent(new BeanComponentDefinition(builder
                .getBeanDefinition(), KaWebSocketBean.POST_BEAN_PROCESSOR_ID));
    }
}

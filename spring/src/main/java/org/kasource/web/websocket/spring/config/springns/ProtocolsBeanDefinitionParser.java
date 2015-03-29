package org.kasource.web.websocket.spring.config.springns;


import java.util.List;

import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ProtocolsBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        if(element.getLocalName().toLowerCase().contains("text")) {
            return TextProtocolHandlerConfigImpl.class;
        } else {
            return BinaryProtocolHandlerConfigImpl.class;
        }
        
    }
    
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        if(element.getLocalName().toLowerCase().contains("text")) {
            element.setAttribute(ID_ATTRIBUTE, KaWebSocketBean.TEXT_PROTOCOLS_CONFIG_ID);
        } else {
            element.setAttribute(ID_ATTRIBUTE, KaWebSocketBean.BINARY_PROTOCOLS_CONFIG_ID);
        }
        bean.setLazyInit(false);
        String ref = element.getAttribute("defaultProtocolRef");
        if(ref != null && !ref.isEmpty()) {
            bean.addPropertyReference("defaultProtocol", ref);
        }
        ManagedMap<String, RuntimeBeanReference> protocols = getProtocols(element);
        
        if(!protocols.isEmpty()) {
            MutablePropertyValues props =  bean.getBeanDefinition().getPropertyValues();
            PropertyValue value = props.getPropertyValue("protocolHandlers");
            if (value == null) {            
                props.addPropertyValue("protocolHandlers", protocols);
            } else {
               value.setSource(protocols);
            }
        }
        
       
    }
     
    
    private ManagedMap<String, RuntimeBeanReference> getProtocols(Element protocolHandler) {
        ManagedMap<String, RuntimeBeanReference> protocolList = new ManagedMap<String, RuntimeBeanReference>();
        List<Element> protocols = WebSocketXmlNamespaceHandler.getChildElementsByName(protocolHandler, "protocol");
        if(!protocols.isEmpty()) {
            for (Element protocolRefElement : protocols) { 
                protocolList.put(protocolRefElement.getAttribute("name"), new RuntimeBeanReference(protocolRefElement.getAttributes().getNamedItem("ref").getTextContent()));
            }
        }
        return protocolList;
    }
    
    
    
   
}

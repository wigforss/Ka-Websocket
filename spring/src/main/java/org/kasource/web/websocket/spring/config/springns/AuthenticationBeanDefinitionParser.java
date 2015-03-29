package org.kasource.web.websocket.spring.config.springns;


import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class AuthenticationBeanDefinitionParser  extends AbstractSingleBeanDefinitionParser {
    
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return AuthenticationConfig.class;
    }
    
    @Override
    protected void doParse(Element element, ParserContext pc,
            BeanDefinitionBuilder bean) {
        element.setAttribute(ID_ATTRIBUTE, KaWebSocketBean.AUTHENTICATION_CONFIG_ID);
       
       
        String ref = element.getAttribute("ref");
        
        if(ref != null && !ref.isEmpty()) {
            bean.addPropertyReference("authenticationProvider", ref);
        }
     
       
    }
   
    
   
}

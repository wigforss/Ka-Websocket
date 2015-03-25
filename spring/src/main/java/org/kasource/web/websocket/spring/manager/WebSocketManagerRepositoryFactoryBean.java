package org.kasource.web.websocket.spring.manager;



import javax.servlet.ServletContext;

import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.manager.WebSocketManagerRepositoryImpl;
import org.kasource.web.websocket.protocol.ProtocolRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

public class WebSocketManagerRepositoryFactoryBean  implements  FactoryBean<WebSocketManagerRepository>, ServletContextAware, ApplicationContextAware { 
    
    private ApplicationContext applicationContext;
    private ServletContext servletContext;
   
 

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        
    }




    @Override
    public WebSocketManagerRepository getObject() throws Exception {
        AuthenticationConfig authConfig = null;
        try {
             authConfig = applicationContext.getBean(AuthenticationConfig.class);
        } catch (Exception e) {
           if(applicationContext.getBeanNamesForType(AuthenticationConfig.class).length > 0) {
               throw e;
           }
        }
        
        
        WebSocketManagerRepositoryImpl managerRepo = new WebSocketManagerRepositoryImpl();
        managerRepo.setServletContext(servletContext);
        
        return managerRepo;
    }




    @Override
    public Class<?> getObjectType() {
        return WebSocketManagerRepository.class;
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

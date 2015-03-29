package org.kasource.web.websocket.spring.config.springns.support;



import javax.servlet.ServletContext;

import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.manager.WebSocketManagerRepositoryImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.context.ServletContextAware;

public class WebSocketManagerRepositoryFactoryBean  implements  FactoryBean<WebSocketManagerRepository>, ServletContextAware { 
    
   
    private ServletContext servletContext;
   
 

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        
    }

    @Override
    public WebSocketManagerRepository getObject() throws Exception {
        
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
   
}

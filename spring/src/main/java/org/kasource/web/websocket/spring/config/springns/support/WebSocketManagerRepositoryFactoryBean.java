package org.kasource.web.websocket.spring.config.springns.support;



import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.channel.client.ClientChannelRepositoryImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.context.ServletContextAware;

public class WebSocketManagerRepositoryFactoryBean  implements  FactoryBean<ClientChannelRepository>, ServletContextAware { 
    
   
    private ServletContext servletContext;
   
 

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        
    }

    @Override
    public ClientChannelRepository getObject() throws Exception {
        
        ClientChannelRepositoryImpl managerRepo = new ClientChannelRepositoryImpl();
        managerRepo.setServletContext(servletContext);
        
        return managerRepo;
    }

    @Override
    public Class<?> getObjectType() {
        return ClientChannelRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
   
}

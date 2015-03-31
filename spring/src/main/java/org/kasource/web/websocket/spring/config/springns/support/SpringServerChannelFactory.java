
package org.kasource.web.websocket.spring.config.springns.support;


import javax.servlet.ServletContext;
import org.kasource.web.websocket.channel.server.ServerChannelFactoryImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

/**
 * 
 * @author rikardwi
 **/
public class SpringServerChannelFactory extends ServerChannelFactoryImpl implements  ServletContextAware, InitializingBean {
    
    private ServletContext servletContext;
    
    /**
     * Set the servlet context to use.
     **/
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        
    }

    /**
     * On initialization, create all web sockets currently available in
     * the servlet context.
     **/
    @Override
    public void afterPropertiesSet() throws Exception {       
        initialize(servletContext);
    }


}

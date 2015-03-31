package org.kasource.web.websocket.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

/**
 * Standard implementation of WebSocketManagerRepository.
 * 
 * @author rikardwi
 **/
public class WebSocketManagerRepositoryImpl implements WebSocketManagerRepository {
    private Map<String, WebSocketManager> managers = new ConcurrentHashMap<String, WebSocketManager>();
    private ServletContext servletContext;
 

    public WebSocketManagerRepositoryImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public WebSocketManagerRepositoryImpl() {

    }

    @Override
    public WebSocketManager getWebSocketManager(String url) {
        if (!managers.containsKey(url)) {
            WebSocketManagerImpl manager = new WebSocketManagerImpl();
          
            managers.put(url, manager);
            servletContext.setAttribute(ATTRIBUTE_PREFIX + url, manager);
            return manager;
        }
        return managers.get(url);
    }



    /**
     * @param servletContext
     *            the servletContext to set
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

   

    

    

}

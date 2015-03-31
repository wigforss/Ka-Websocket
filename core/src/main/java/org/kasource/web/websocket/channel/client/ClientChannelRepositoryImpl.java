package org.kasource.web.websocket.channel.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

/**
 * Standard implementation of WebSocketManagerRepository.
 * 
 * @author rikardwi
 **/
public class ClientChannelRepositoryImpl implements ClientChannelRepository {
    private Map<String, ClientChannel> channels = new ConcurrentHashMap<String, ClientChannel>();
    private ServletContext servletContext;
 

    public ClientChannelRepositoryImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ClientChannelRepositoryImpl() {

    }

    @Override
    public ClientChannel getClientChannel(String url) {
        if (!channels.containsKey(url)) {
            ClientChannelImpl manager = new ClientChannelImpl();
          
            channels.put(url, manager);
            servletContext.setAttribute(ATTRIBUTE_PREFIX + url, manager);
            return manager;
        }
        return channels.get(url);
    }



    /**
     * @param servletContext
     *            the servletContext to set
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

   

    

    

}

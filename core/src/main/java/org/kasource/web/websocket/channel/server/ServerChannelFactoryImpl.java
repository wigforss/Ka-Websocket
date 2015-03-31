package org.kasource.web.websocket.channel.server;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.client.ClientChannel;
import org.kasource.web.websocket.channel.client.ClientChannelRepository;
import org.kasource.web.websocket.listener.WebSocketEventListener;


/**
 * Web Socket Channel Factory standard implementation.
 * 
 * Server Channels returned might not be fully initialized since client
 * channels are lazily created.
 * 
 * Provides access to WebSocketChannels.
 * 
 * @author rikardwi
 **/
public class ServerChannelFactoryImpl implements ServerChannelFactory {
    
    
    private Map<String, ServerChannelImpl> serverChannels = new ConcurrentHashMap<String, ServerChannelImpl>();
    protected ServletContext servletContext;
    private Map<String, List<WebSocketEventListener>> lazyListeners = new ConcurrentHashMap<String, List<WebSocketEventListener>>();
 
   
    
    
    /**
     * On initialization, create all web sockets currently available in
     * the servlet context and listen for events when web sockets becomes
     * available in the servlet context.
     * 
     * @param servletContext The servlet context to listen to.
     **/
    @Override
    public void initialize(ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;
        
        Enumeration<String> attributeEnum = servletContext.getAttributeNames();
        while (attributeEnum.hasMoreElements()) {
            String attributeName = attributeEnum.nextElement();
            addClientChannelFromAttribute(attributeName, servletContext.getAttribute(attributeName));
        }
        
        servletContext.setAttribute(ServerChannelFactory.class.getName(), this);
    }

    
    
    /**
     * Returns the ServerChannel for a URL.
     * 
     * Note that if this method is invoked before such a 
     * ClientChannel is available, an uninitialized ServerChannel will be returned, 
     * which might be initialized at a later point in time once the first client connects.
     * 
     * @param url    URL of the Server Channel to return.
     * 
     * @return A Server Channel representing URL supplied.
     **/
    public ServerChannelImpl get(String url) {
        if (serverChannels.containsKey(url)) {
            return serverChannels.get(url);
        }
        
        ClientChannel clientChannel = (ClientChannel) servletContext.getAttribute(url);
        ServerChannelImpl serverChannel = null;
        if (clientChannel == null) {
             serverChannel = new ServerChannelImpl();
        } else {
             serverChannel = new ServerChannelImpl(url, clientChannel);
        }
        serverChannels.put(url, serverChannel);
        
        return serverChannel;
    }

    
    
    /**
     * Register an event listener for a URL.
     * 
     * @param url    URL of the socket to listen to.
     * @param listener      Listener instance.
     **/
    public void listenTo(String url, WebSocketEventListener listener) {
        if (url.contains("*")) {
            String urlPattern = url.replace("*", ".*");
            for (Map.Entry<String, ServerChannelImpl> entry : serverChannels.entrySet()) {
                if (entry.getKey().matches(urlPattern)) {
                    entry.getValue().addListener(listener);
                }
            }
            addLazyListener(url, listener);
        } else {
            ServerChannelImpl serverChannel = get(url);
            if (serverChannel == null) {
                addLazyListener(url, listener);
            }
            serverChannel.addListener(listener);
        }
    }

   
    
    private void addLazyListener(String url, WebSocketEventListener listener) {
        List<WebSocketEventListener> listeners = lazyListeners.get(url);
        if (listeners == null) {
            listeners = new ArrayList<WebSocketEventListener>();
            lazyListeners.put(url, listeners);
        }
        listeners.add(listener);
    }
    
    
    /**
     * Adds a web socket from a published servlet context attribute.
     * 
     * @param attributeName     Name of an attribute
     * @param attributeValue    Value of an attribute
     **/
    public void addClientChannelFromAttribute(String attributeName, Object attributeValue) {
        if (attributeValue instanceof ClientChannel) {
            String url = attributeName.substring(ClientChannelRepository.ATTRIBUTE_PREFIX.length());
            ServerChannelImpl serverChannel = serverChannels.get(url);
            if (serverChannel != null) {
                serverChannel.initialize(url, (ClientChannel) attributeValue);
            } else {
                serverChannel = new ServerChannelImpl(url, (ClientChannel) attributeValue);
                serverChannels.put(url, serverChannel);
                addLazyListeners(url, serverChannel);
            }
        }
    }
    
    /**
     * Add lazily registered listeners.
     * 
     * @param name      Name of the WebSocket
     * @param webSocket WebSocket object.
     **/
    private void addLazyListeners(String url, ServerChannelImpl webSocket) {
        for (Map.Entry<String, List<WebSocketEventListener>> entry : lazyListeners.entrySet()) {
            boolean hasWildcard = isWildcardName(entry.getKey());
            String urlPattern = entry.getKey().replace("*", ".*");
            if (url.matches(urlPattern)) {
                Set<WebSocketEventListener> toRemove = new HashSet<WebSocketEventListener>();
                for (WebSocketEventListener listener : entry.getValue()) {
                    webSocket.addListener(listener);
                    if (!hasWildcard) {
                        toRemove.add(listener);
                    }
                }
                entry.getValue().removeAll(toRemove);
            }
            
        }      
    }

    private boolean isWildcardName(String channelUrl) {
        return channelUrl.contains("*");
    }
    
    

}

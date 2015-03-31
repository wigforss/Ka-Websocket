package org.kasource.web.websocket.event;

import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.channel.server.ServerChannel;
import org.kasource.web.websocket.client.WebSocketClient;

/**
 * Base class for client related web socket events.
 * 
 * @author rikardwi
 **/
public abstract class ClientEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;

    private final WebSocketClient client;
    private final ParameterBinder parameterBinder;
    
  
    
    /**
     * Constructor.
     * 
     * @param websocket The source web socket.
     * @param clientId  Client ID
     * @param username  Username of the client
     **/
    public ClientEvent(ServerChannel websocket, WebSocketClient client, ParameterBinder parameterBinder) {
        super(websocket);
        this.client = client;
        this.parameterBinder = parameterBinder;
    }
    
    
    

    /**
     * @return the client
     */
    public WebSocketClient getClient() {
        return client;
    }


    /**
     * @return the parameterBinder
     */
    public ParameterBinder getParameterBinder() {
        return parameterBinder;
    }

    
    
}

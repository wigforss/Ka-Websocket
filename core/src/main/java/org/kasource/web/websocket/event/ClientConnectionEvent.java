package org.kasource.web.websocket.event;

import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.channel.server.ServerChannel;
import org.kasource.web.websocket.client.WebSocketClient;

/**
 * Event emitted when a client has connected to a web socket.
 * 
 * @author rikardwi
 **/
public class ClientConnectionEvent extends ClientEvent {
    private static final long serialVersionUID = 1L;
    

    /**
     * Constructor.
     * 
     * @param websocket             The websocket that a client connected to.
     * @param clientId              The ID of the client 
     * @param connectionParameters  The connection parameters
     **/
    public ClientConnectionEvent(ServerChannel websocket, 
                                          WebSocketClient client, 
                                          ParameterBinder parameterBinder) {
        super(websocket, client, parameterBinder);
        
    }

   
    
   

}

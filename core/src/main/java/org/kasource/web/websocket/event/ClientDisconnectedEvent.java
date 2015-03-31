package org.kasource.web.websocket.event;

import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.channel.server.ServerChannel;
import org.kasource.web.websocket.client.WebSocketClient;

/**
 * Event emitted when a client was disconnected.
 * 
 * @author rikardwi
 **/
public class ClientDisconnectedEvent extends ClientEvent {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param webSocket The web socket the client closed connection to
     * @param clientId  The ID of the client who closed the connection
     */
    public ClientDisconnectedEvent(ServerChannel webSocket, WebSocketClient client, ParameterBinder parameterBinder) {
        super(webSocket, client, parameterBinder);
    }
}

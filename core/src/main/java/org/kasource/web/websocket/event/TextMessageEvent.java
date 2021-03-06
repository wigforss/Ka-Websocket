package org.kasource.web.websocket.event;


import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.channel.server.ServerChannel;
import org.kasource.web.websocket.client.WebSocketClient;

/**
 * Event emitted when a text message has been received from 
 * a client.
 * 
 * @author rikardwi
 **/
public class TextMessageEvent extends ClientEvent {

    private static final long serialVersionUID = 1L;
    
    private final String message;
   
    /**
     * Constructor.
     * 
     * @param socket    web socket the message was sent to.
     * @param message   The message sent.
     * @param clientId  The ID of client who sent the message
     **/
    public TextMessageEvent(ServerChannel socket, 
                                     String message, 
                                     WebSocketClient client,
                                     ParameterBinder parameterBinder) {
        super(socket, client, parameterBinder);
        this.message = message;
          
    }
    
   
    /**
     * Returns the message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    
    
}

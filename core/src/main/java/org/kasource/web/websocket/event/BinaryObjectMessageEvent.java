package org.kasource.web.websocket.event;


import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.channel.server.ServerChannel;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.protocol.ProtocolHandler;


/**
 * Event emitted when a binary message has been received from 
 * a client.
 * 
 * @author rikardwi
 **/
public class BinaryObjectMessageEvent extends BinaryMessageEvent {

    private static final long serialVersionUID = 1L;
    
 
    private final ProtocolHandler<byte[]> protocolHandler;
   
    /**
     * Constructor.
     * 
     * @param socket    web socket the message was sent to.
     * @param message   The message sent.
     * @param clientId  The ID of client who sent the message
     **/
    public BinaryObjectMessageEvent(ServerChannel socket, 
                                               byte[] message,
                                               WebSocketClient client,
                                               ProtocolHandler<byte[]> protocolHandler,
                                               ParameterBinder parameterBinder) {
        super(socket, message, client, parameterBinder);      
        this.protocolHandler = protocolHandler;
    }
    
    

    /**
     * Returns the message as Object.
     * 
     * @return the message
     */
    public <T> T getMessageAsObject(Class<T> ofType) {
        return protocolHandler.toObject(getMessage(), ofType);
    }



    /**
     * @return the protocolHandler
     */
    public ProtocolHandler<byte[]> getProtocolHandler() {
        return protocolHandler;
    }

  
    

    
}

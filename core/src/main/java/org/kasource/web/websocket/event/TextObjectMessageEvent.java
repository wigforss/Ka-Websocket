package org.kasource.web.websocket.event;

import org.kasource.commons.reflection.parameter.ParameterBinder;
import org.kasource.web.websocket.channel.server.ServerChannel;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.protocol.ConversionException;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class TextObjectMessageEvent  extends TextMessageEvent {
    private static final long serialVersionUID = 1L;
   
    private final ProtocolHandler<String> protocolHandler;
   
    /**
     * Constructor.
     * 
     * @param socket    web socket the message was sent to.
     * @param message   The message sent.
     * @param clientId  The ID of client who sent the message
     **/
    public TextObjectMessageEvent(ServerChannel socket, 
                                     String message, 
                                     WebSocketClient client,
                                     ProtocolHandler<String> protocolHandler,
                                     ParameterBinder parameterBinder) {
        super(socket, message, client, parameterBinder);
        this.protocolHandler = protocolHandler;
          
    }
    
   
    /**
     * Returns the message as Object.
     * 
     * @return the message
     */
    public <T> T getMessageAsObject(Class<T> ofType) throws ConversionException {
        return protocolHandler.toObject(getMessage(), ofType);
    }


    /**
     * @return the protocolHandler
     */
    public ProtocolHandler<String> getProtocolHandler() {
        return protocolHandler;
    }
}

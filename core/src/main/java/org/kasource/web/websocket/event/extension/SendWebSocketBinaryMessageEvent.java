package org.kasource.web.websocket.event.extension;


import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.client.RecipientType;
import org.kasource.web.websocket.event.WebSocketClientEvent;
import org.kasource.web.websocket.event.WebSocketEvent;


/**
 * Event for sending a binary message to a specific client.
 * 
 * @author rikardwi
 **/
public class SendWebSocketBinaryMessageEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;
    
    private final byte[] message;
    private final String recipient;
    private final RecipientType recipientType;
    
    /**
     * Constructor.
     * 
     * @param socket    The web socket to send message to
     * @param message   The message to send
     * @param clientId  The recipient of the message
     **/
    public SendWebSocketBinaryMessageEvent(WebSocketChannel socket, byte[] message, String recipient, RecipientType recipientType) {
        super(socket);
        this.message = message;
        this.recipient = recipient;
        this.recipientType = recipientType;
    }
    
    public SendWebSocketBinaryMessageEvent(WebSocketClientEvent event, byte[] message, RecipientType recipientType) {
        this(event.getSource(), 
                    message, 
                    recipientType == RecipientType.CLIENT_ID ? event.getClientId() : event.getUsername(), 
                    recipientType);
    }

    /**
     * Returns the binary message to send.
     * 
     * @return the binary message to send.
     */
    public byte[] getMessage() {
        return message;
    }

    /**
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @return the recipientType
     */
    public RecipientType getRecipientType() {
        return recipientType;
    }

    
  
    

    
}
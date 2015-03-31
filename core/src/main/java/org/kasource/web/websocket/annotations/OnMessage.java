package org.kasource.web.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for binding the message to a method argument.
 * 
 * Parameters are bound in the following order
 * <ul>
 * <li>Any one parameter annotated with @Payload</li>
 * <li>Any String parameter annotated with @Username, @ClientId, @ClientIp, @RequestParameter, @Header, @SystemProperty or @Environment
 * <li>Parameter of type WebSocketTextMessageEvent (if text message)</li>
 * <li>Parameter of type WebSocketBinaryMessageEvent (if binary message)</li>
 * <li>Parameter of type String - message (if text message)</li>
 * <li>Parameter of type byte[] - message (if binary message)</li>
 * <li>Parameter of type WebSocketChannel</li>
 * <li>Parameter of type WebSocketClient</li>
 * </ul>
 * <p/>
 * If a text or binary protocol is used an automatic conversion will be performed to the type of the method parameter
 * annotated with @Payload.
 * <p/>
 * If the method returns a value (not null), that value will be sent back to the client sending the message, unless the
 * method is annotated with @Broadcast then it is sent to all clients on the clients channel.
 * 
 *@{code
 * 
 * @WebSocketListener("/chat")
 * public class ChatServer {
 * 
 *  @OnMessage
 *  @Broadcast
 *  public String onMessage(String message, @Header("Content-Type") String contentType) {
 *   ...
 *   return "Broadcast message"
 *  }
 *  
 *  @OnMessage
 *  public void onMessage(byte[] message, @RequestParameter("user") String username) {
 *   ...
 *  }
 *  
 *  @WebSocketListener("/chat2") // chat2 is configured with a protocol.
 *  @OnMessage
 *  public void onMessage(@Payload Message message) {
 *   ...
 *  }
 * }
 * 
 * }
 * 
 * 
 * @author rikardwi
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnMessage {
    
}

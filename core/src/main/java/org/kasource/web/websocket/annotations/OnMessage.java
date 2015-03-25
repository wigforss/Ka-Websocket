package org.kasource.web.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for binding the message to a method argument.
 * 
 * The argument of the method annotated could be String or byte[] for plain messages.
 * 
 * If a text or binary protocol is used an automatic conversion will be performed to the type of the method argument.
 * 
 * If more information than the actual message is needed use the standard method annotation @OnWebSocketEvent instead,
 * which gives you all the details of the event such as clientId and the source WebSocketChannel.
 * 
 *@{code
 * 
 * @WebSocketListener("/chat")
 * public class ChatServer {
 * 
 *  @OnMessage
 *  @Broadcast
 *  public String onMessage(String message) {
 *   ...
 *   return "Broadcast message"
 *  }
 *  
 *  @OnMessage
 *  public void onMessage(byte[] message) {
 *   ...
 *  }
 *  
 *  @WebSocketListener("/chat2") // chat2 is configured with a protocol.
 *  @OnMessage
 *  public void onMessage(Message message) {
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
@WebSocketEventAnnotation
public @interface OnMessage {
    
}

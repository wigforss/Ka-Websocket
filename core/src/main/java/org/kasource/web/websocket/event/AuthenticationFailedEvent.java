package org.kasource.web.websocket.event;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.channel.server.ServerChannel;

public class AuthenticationFailedEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;

    private final HandshakeRequest request;
    private final Throwable cause;
    private final String username;

    public AuthenticationFailedEvent(ServerChannel websocket, String username, HandshakeRequest request, Throwable cause) {
        super(websocket);
        this.cause = cause;
        this.request = request;
        this.username = username;
    }

    /**
     * @return the cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * @return the request
     */
    public HandshakeRequest getRequest() {
        return request;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    
}

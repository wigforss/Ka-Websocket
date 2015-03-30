package org.kasource.web.websocket.event;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.channel.WebSocketChannel;

public class WebSocketAuthenticationFailedEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;

    private final HttpServletRequest request;
    private final Throwable cause;
    private final String username;

    public WebSocketAuthenticationFailedEvent(WebSocketChannel websocket, String username, HttpServletRequest request, Throwable cause) {
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
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    
}

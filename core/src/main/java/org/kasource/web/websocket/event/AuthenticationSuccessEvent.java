package org.kasource.web.websocket.event;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.channel.server.ServerChannel;

public class AuthenticationSuccessEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;

    private final HttpServletRequest request;
    private final String username;
    public AuthenticationSuccessEvent(ServerChannel websocket, String username, HttpServletRequest request) {
        super(websocket);
        this.username = username;
        this.request = request;
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

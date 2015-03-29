package org.kasource.web.websocket.security;

public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String username;
    private final String clientIp;
    
    public AuthenticationException(String message, String username, String clientIp) {
        super(message);
        this.username = username;
        this.clientIp = clientIp;
    }
    
    public AuthenticationException(String message, Throwable cause, String username, String clientIp) {
        super(message, cause);
        this.username = username;
        this.clientIp = clientIp;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the clientIp
     */
    public String getClientIp() {
        return clientIp;
    }

}

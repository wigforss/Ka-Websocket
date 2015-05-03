package org.kasource.web.websocket.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {
    private static final String DEFAULT_USERNAME_KEY = "username";
    private static final String DEFAULT_PASSWORD_KEY = "password";
    
    private boolean headerBased;
    private String usernameKey = DEFAULT_USERNAME_KEY;
    private String passwordKey = DEFAULT_PASSWORD_KEY;
    
    protected String getUsername(HandshakeRequest request) {
        String username = null;
        if (headerBased) {
            username = getHeader(request, usernameKey);
        } else {
            username = getParameter(request, usernameKey);
        }
      
        return username;
    }
    
    protected String getPassword(HandshakeRequest request) {
        String password = null;
        if (headerBased) {
            password = getHeader(request, passwordKey);
        } else {
            password = getParameter(request, passwordKey);
        }
       
        return password;
    }

    protected String getHeader(HandshakeRequest request, String headerName) {    
        List<String> headerValues = request.getHeaders().get(headerName);
        if (headerValues != null && !headerValues.isEmpty()) {
            return headerValues.get(0);
        }
        return null;       
    }
    
    protected String getParameter(HandshakeRequest request, String parameterName) {
        List<String> parametersValues = request.getParameterMap().get(parameterName);
        if (parametersValues != null && !parametersValues.isEmpty()) {
            return parametersValues.get(0);
        } 
        return null;
    }
    
   
    
    
    /**
     * @return the headerBased
     */
    public boolean isHeaderBased() {
        return headerBased;
    }

    /**
     * @param headerBased the headerBased to set
     */
    public void setHeaderBased(boolean headerBased) {
        this.headerBased = headerBased;
    }

    /**
     * @return the usernameKey
     */
    public String getUsernameKey() {
        return usernameKey;
    }

    /**
     * @param usernameKey the usernameKey to set
     */
    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

    /**
     * @return the passwordKey
     */
    public String getPasswordKey() {
        return passwordKey;
    }

    /**
     * @param passwordKey the passwordKey to set
     */
    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }
}

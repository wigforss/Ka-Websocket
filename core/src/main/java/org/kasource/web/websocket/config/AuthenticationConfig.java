package org.kasource.web.websocket.config;

import org.kasource.web.websocket.security.AuthenticationProvider;

public class AuthenticationConfig {
    private AuthenticationProvider authenticationProvider;
    
   
    /**
     * @return the defaultAuthenticationProvider
     */
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    /**
     * @param defaultAuthenticationProvider the defaultAuthenticationProvider to set
     */
    public void setAuthenticationProvider(AuthenticationProvider defaultAuthenticationProvider) {
        this.authenticationProvider = defaultAuthenticationProvider;
    }

}

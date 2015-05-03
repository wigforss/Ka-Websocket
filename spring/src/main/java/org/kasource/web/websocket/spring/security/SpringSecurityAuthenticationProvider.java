package org.kasource.web.websocket.spring.security;


import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityAuthenticationProvider implements AuthenticationProvider {

    @Override
    public String authenticate(HandshakeRequest request) throws AuthenticationException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            throw new AuthenticationException("User not authenticateded", "Anonymous");
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new AuthenticationException("User not authenticateded", "Anonymous");
        }
        if (authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            throw new AuthenticationException("User not authenticateded", authentication.getName());
        }
    }

}

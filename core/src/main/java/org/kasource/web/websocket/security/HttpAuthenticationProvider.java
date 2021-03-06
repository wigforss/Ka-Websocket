package org.kasource.web.websocket.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

public class HttpAuthenticationProvider implements AuthenticationProvider {

    @Override
    public String authenticate(HandshakeRequest request) throws AuthenticationException {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            return principal.getName();
        }
        throw new AuthenticationException("User not authenticateded", "Anonymous");
    }

}

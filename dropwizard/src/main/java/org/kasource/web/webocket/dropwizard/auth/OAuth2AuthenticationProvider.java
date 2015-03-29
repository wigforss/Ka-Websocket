package org.kasource.web.webocket.dropwizard.auth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.security.AuthenticationProvider;

import com.google.common.base.Optional;

import io.dropwizard.auth.Authenticator;

/**
 * Authentication Provider for DropWizard OAuth2 Authenticator.
 * 
 * @author rikardwi
 *
 * @param <T> The user / principal type
 */
public class OAuth2AuthenticationProvider<T> implements AuthenticationProvider {
    private static final String OAUTH_PREFIX = "Bearer ";
    private Authenticator<String, T> authenticator;
    
    public OAuth2AuthenticationProvider(Authenticator<String, T> authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public String authenticate(HttpServletRequest request) throws AuthenticationException {
        String accessToken = getAccessToken(request);
        if (accessToken != null) {
            try {
                Optional<T> user = authenticator.authenticate(accessToken);
                if (user.isPresent()) {
                    return user.toString();
                } else {
                    throw new AuthenticationException("User authention failed", "Anonymous", request.getRemoteAddr());
                }
            } catch (io.dropwizard.auth.AuthenticationException e) {
                throw new AuthenticationException("User authention failed", "Anonymous", request.getRemoteAddr());
            }
        } else {
            throw new AuthenticationException("Could not parse Bearer Authorization header", "Anonymous", request.getRemoteAddr());
        }
    }
    
    private String getAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(OAUTH_PREFIX)) {
            return authHeader.substring(OAUTH_PREFIX.length());
        } else {
            return null;
        }
    }
}

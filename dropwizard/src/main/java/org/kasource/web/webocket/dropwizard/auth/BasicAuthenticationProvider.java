package org.kasource.web.webocket.dropwizard.auth;

import java.nio.charset.Charset;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.codec.binary.Base64;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.security.AuthenticationProvider;

import com.google.common.base.Optional;

/**
 * Authentication Provider for DropWizard Basic Auth Authenticator.
 * 
 * @author rikardwi
 *
 * @param <T> The user / principal type
 */
public class BasicAuthenticationProvider<T> implements AuthenticationProvider {
    private static final String BASIC_AUTH_PREFIX = "Basic ";
    
    private Authenticator<BasicCredentials, T> authenticator;
    
    public BasicAuthenticationProvider(Authenticator<BasicCredentials, T> authenticator) {
        this.authenticator = authenticator;
    }
    
    @Override
    public String authenticate(HttpServletRequest request) throws AuthenticationException {
        BasicCredentials credentials = getCredentials(request);
        if (credentials == null) {
            throw new AuthenticationException("Could not parse Basic Authorization header", "Anonymous", request.getRemoteAddr());
        }
        try {
            Optional<T> user = authenticator.authenticate(credentials);
            if (!user.isPresent()) {
                throw new AuthenticationException("User authention failed", credentials.getUsername(), request.getRemoteAddr());
            }
        } catch (io.dropwizard.auth.AuthenticationException e) {
            throw new AuthenticationException("User authention failed", e, credentials.getUsername(), request.getRemoteAddr());
        }
       
         return credentials.getUsername();
    }
    
    private BasicCredentials getCredentials(HttpServletRequest request) {
        String basicCredentials = getBasicCredentials(request);
        if (basicCredentials != null) {
            String[] split = basicCredentials.split(":");
            BasicCredentials credentials = new BasicCredentials(split[0], split[1]);
            return credentials;
        }
        return null;
    }
    
    private String getBasicCredentials(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            return new String(Base64.decodeBase64(authHeader.substring(BASIC_AUTH_PREFIX.length())), Charset.forName("UTF-8"));
            
        }
        return null;
    }

}

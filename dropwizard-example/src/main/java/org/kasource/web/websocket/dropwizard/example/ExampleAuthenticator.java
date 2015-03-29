package org.kasource.web.websocket.dropwizard.example;

import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class ExampleAuthenticator implements Authenticator<BasicCredentials, User> {

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("admin".equals(credentials.getUsername()) 
                    && "admin".equals(credentials.getPassword())) {
            return Optional.of(new User());
        }
        return Optional.absent();
    }

}

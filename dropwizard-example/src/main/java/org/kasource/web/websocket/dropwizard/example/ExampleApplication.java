package org.kasource.web.websocket.dropwizard.example;

import org.eclipse.jetty.server.session.SessionHandler;
import org.kasource.web.webocket.dropwizard.WebSocketInitializer;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.auth.oauth.OAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ExampleApplication extends Application<ExampleConfiguration> {

    public static void main(String[] args) throws Exception {
        new ExampleApplication().run(args);
    }
    
    @Override
    public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
      
        bootstrap.addBundle(new AssetsBundle("/assets/", "/assets/"));
    }

    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
        Authenticator<BasicCredentials, User> authenticator = new ExampleAuthenticator();
        WebSocketInitializer<ExampleConfiguration> webSocketInitializer =
                    new WebSocketInitializer.Builder<ExampleConfiguration>(configuration, environment)
                        .basicAuthenticator(authenticator).build();
         environment.jersey().register(new PingResource());
        webSocketInitializer.onRun();
        webSocketInitializer.addWebocket(new ChatServer());
        environment.servlets().setSessionHandler(new SessionHandler());
        environment.jersey().register(new BasicAuthProvider<User>(authenticator, "My Realm"));
      
    }

}

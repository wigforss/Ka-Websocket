package org.kasource.web.websocket.dropwizard.example;

import org.kasource.web.webocket.dropwizard.config.WebSocketConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class ExampleConfiguration extends Configuration {
    @JsonProperty
    private WebSocketConfiguration websocket;

    /**
     * @return the websocket
     */
    public WebSocketConfiguration getWebsocket() {
        return websocket;
    }
    
    
}

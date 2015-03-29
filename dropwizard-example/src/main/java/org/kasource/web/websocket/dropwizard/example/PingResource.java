package org.kasource.web.websocket.dropwizard.example;

import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping")
@Produces(MediaType.TEXT_PLAIN)
public class PingResource {

    @GET
    public String ping(@Auth User user) {
        return "pong";
    }
}

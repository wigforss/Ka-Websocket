package org.kasource.web.websocket.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kasource.web.websocket.client.id.ClientIdGenerator;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientId {
    
    /**
     * Replaces the default client ID generator with this implementation.
     * 
     * A new instance is created by invoking the public empty constructor, unless the bean attribute is set.
     **/
    Class<? extends ClientIdGenerator> value();
    
    /**
     * Read user name for the upgrade HTTP request header (true) or from a request parameter (false).
     * 
     * Default false.
     **/
    boolean usernameFromHeader() default false;
    
    /**
     * Name of the request header or request parameter that holds the user name.
     * 
     * Default is username
     **/
    String usernameKey() default "username";
    
    /**
     * Looks up the ID generator via a bean name using a Bean Resolver. 
     * 
     * Note, requires a dependency injection framework module to be present (Sping, Guice or CDI), setting
     * this attribute without a DI framework present will cause an exception to be thrown.
     **/
    String bean() default "";
}

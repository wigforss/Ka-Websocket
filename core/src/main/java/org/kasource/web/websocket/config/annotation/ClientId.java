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
     * Looks up the ID generator via a bean name using a Bean Resolver. 
     * 
     * Note, requires a dependency injection framework module to be present (Sping, Guice or CDI), setting
     * this attribute without a DI framework present will cause an exception to be thrown.
     **/
    String bean() default "";
}

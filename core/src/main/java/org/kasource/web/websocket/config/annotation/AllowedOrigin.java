package org.kasource.web.websocket.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add Origin check for incoming HTTP upgrade requests,
 * 
 * @author rikardwi
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedOrigin {
    
    /**
     * White list of Origins to accept client request for.
     **/
    String[] value();
}

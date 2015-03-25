package org.kasource.web.websocket.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Registers an new WebSocket end-point.
 * 
 * @author rikardwi
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocket {
    
    /**
     * Path to bind WebSocket to
     **/
    String value();
    
    /**
     * If path end with a * lets clients connect to a
     * sub path as its own independent channel.
     **/
    boolean dynamicAddressing() default true;
}

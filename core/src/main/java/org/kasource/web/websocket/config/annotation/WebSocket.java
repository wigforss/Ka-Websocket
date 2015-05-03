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
    
    /**
     * Timeout when sending async messages in milliseconds.
     * 
     * Note: might not be support for all application servers.
     * 
     * Default values is what your application servers default value is set to.
     **/
    long asyncSendTimeoutMillis() default -1L;
    
    /**
     * Maximum binary message buffer size in bytes.
     * 
     * Note: might not be support for all application servers.
     * 
     * Default values is what your application servers default value is set to.
     **/
    int maxBinaryMessageBufferSizeByte() default -1;
    
    /**
     * Maximum session idle timeout in milliseconds.
     * 
     * Note: might not be support for all application servers.
     * 
     * Default values is what your application servers default value is set to.
     **/
    long maxSessionIdleTimeoutMillis() default -1L;
    
    /**
     * Maximum text message buffer size in bytes.
     * 
     * Note: might not be support for all application servers.
     * 
     * Default values is what your application servers default value is set to.
     **/
    int maxTextMessageBufferSizeByte() default -1;
}

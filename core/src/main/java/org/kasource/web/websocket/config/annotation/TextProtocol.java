package org.kasource.web.websocket.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kasource.web.websocket.protocol.TextProtocolHandler;

/**
 * Add support for additional text sub protocols that can be requested by the client.
 * 
 * @author rikardwi
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TextProtocol {
    
    /**
     * Text protocol name (for example a MIME type name), which can be requested by the client by setting this as its
     * sub protocol.
     **/
    String protocol();

   /**
    * Class name of the protocol handler implementation.
    * 
    * A new instance is created by invoking the public empty constructor, unless the bean attribute is set.
    **/
    Class<? extends TextProtocolHandler> handler();
    
    
    /**
     * Looks up the handler via a bean name using Bean Resolver. 
     * 
     * Note, requires a dependency injection framework module to be present (Sping, Guice or CDI), setting
     * this attribute without a DI framework present will cause an exception to be thrown.
     **/
    String bean() default "";
}

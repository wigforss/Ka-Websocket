package org.kasource.web.websocket.spring.config.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Documented
@Target(ElementType.TYPE)
@Import(SpringKaWebSocket.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableKaWebsocket {

}

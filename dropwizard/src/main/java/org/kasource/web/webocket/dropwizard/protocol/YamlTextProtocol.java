package org.kasource.web.webocket.dropwizard.protocol;

import org.kasource.web.websocket.protocol.ConversionException;
import org.kasource.web.websocket.protocol.TextProtocolHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * YAML text protocol handler.
 * 
 * 
 * @author rikardwi
 */
public class YamlTextProtocol implements TextProtocolHandler {
    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    
    @Override
    public String toMessage(Object payload) throws ConversionException {
        try {
            return mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new ConversionException("Could not convert " + payload + " to String");
         } 
    }

    @Override
    public <T> T toObject(String message, Class<T> payloadType) throws ConversionException {
        
        try {
            return mapper.readValue(message, payloadType);
        } catch (Exception e) {
           throw new ConversionException("Could not convert " + message + " to " + payloadType);
        } 
    }

}

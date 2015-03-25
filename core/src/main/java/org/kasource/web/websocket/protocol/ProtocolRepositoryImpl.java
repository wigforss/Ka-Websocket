package org.kasource.web.websocket.protocol;

import java.util.HashMap;
import java.util.Map;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;

public class ProtocolRepositoryImpl implements ProtocolRepository {
    private Map<String, ProtocolHandler<String>> textProtocols = new HashMap<String, ProtocolHandler<String>>();
    private Map<String, ProtocolHandler<byte[]>> binaryProtocols = new HashMap<String, ProtocolHandler<byte[]>>();
    
    private ProtocolHandler<String> defaultTextProtocol;
    private ProtocolHandler<byte[]> defaultBinaryProtocol;

    public ProtocolRepositoryImpl() {
    }

    public ProtocolRepositoryImpl(ProtocolHandlerConfig<String> textProtocolConfig,
                ProtocolHandlerConfig<byte[]> binaryProtocolConfig) {
        try {
            setTextProtocolHandlerConfig(textProtocolConfig);
            setBinaryProtocolHandlerConfig(binaryProtocolConfig);
        } catch (Exception e) {
            throw new WebSocketConfigException("Could not initialize ProtocolHandlerRepository", e);
        }
    }

    public void setTextProtocolHandlerConfig(ProtocolHandlerConfig<String> textProtocolConfig) {
        if (textProtocolConfig != null) {
            defaultTextProtocol = textProtocolConfig.getDefaultProtocol();
            textProtocols = textProtocolConfig.getProtocolHandlers();
        }
    }

    public void setBinaryProtocolHandlerConfig(ProtocolHandlerConfig<byte[]> binaryProtocolConfig) {
        if (binaryProtocolConfig != null) {
            defaultBinaryProtocol = binaryProtocolConfig.getDefaultProtocol();
            binaryProtocols = binaryProtocolConfig.getProtocolHandlers();
        }
    }

    public void initialize(ProtocolHandlerConfig<String> textProtocolConfig,
                ProtocolHandlerConfig<byte[]> binaryProtocolConfig) throws ClassNotFoundException,
                InstantiationException, IllegalAccessException {
        setTextProtocolHandlerConfig(textProtocolConfig);
        setBinaryProtocolHandlerConfig(binaryProtocolConfig);
    }

    @Override
    public boolean hasProtocol(String protocol) {
        return hasTextProtocol(protocol) || hasBinaryProtocol(protocol);
    }

    private boolean hasTextProtocol(String protocol) {
       
        return textProtocols.containsKey(protocol);
    }

    private boolean hasBinaryProtocol(String protocol) {
        return binaryProtocols.containsKey(protocol);
    }

   

    /**
     * @return the defaultTextProtocol
     */
    @Override
    public ProtocolHandler<String> getDefaultTextProtocol() {
        return defaultTextProtocol;
    }

    /**
     * @return the defaultBinaryProtocol
     */
    @Override
    public ProtocolHandler<byte[]> getDefaultBinaryProtocol() {
        return defaultBinaryProtocol;
    }

    @Override
    public ProtocolHandler<String> getTextProtocol(String protocol, boolean useDefault) {
        
        ProtocolHandler<String> textProtocol = textProtocols.get(protocol);
        if (textProtocol == null) {
            return textProtocol;
        } else if (useDefault) {
            return defaultTextProtocol;
        } else {
            return null;
        }
    }
    
    @Override
    public ProtocolHandler<byte[]> getBinaryProtocol(String protocol, boolean useDefault) {
        ProtocolHandler<byte[]> binaryProtocol = binaryProtocols.get(protocol);
        if (binaryProtocol == null) {
            return binaryProtocol;
        } else if (useDefault) {
            return defaultBinaryProtocol;
        } else {
            return null;
        }
    }

    
    

}

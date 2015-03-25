package org.kasource.web.websocket.protocol;


public interface ProtocolRepository {
    public boolean hasProtocol(String protocol); 
    
    public ProtocolHandler<String> getTextProtocol(String protocol, boolean useDefault);
    
    public ProtocolHandler<byte[]> getBinaryProtocol(String protocol, boolean useDefault);
    
   
    public ProtocolHandler<String> getDefaultTextProtocol();

    
    public ProtocolHandler<byte[]> getDefaultBinaryProtocol();
    
}

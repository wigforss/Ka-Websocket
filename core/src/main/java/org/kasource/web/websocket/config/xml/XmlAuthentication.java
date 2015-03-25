package org.kasource.web.websocket.config.xml;


import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.security.AbstractAuthenticationProvider;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.kasource.web.websocket.security.PassthroughAutenticationProvider;

public class XmlAuthentication {

    
    private AuthenticationProvider provider;
     
    public XmlAuthentication(org.kasource.web.websocket.config.xml.jaxb.AuthenticationProvider config) {
       
        try {
           
           provider = getAuthenticationProvider(config);
            
           
        } catch(Exception e) {
            throw new WebSocketConfigException("Could not load AuthenticationProvider", e);
        }
    }
    

    public AuthenticationProvider getAutenticationProvider() {
        return provider;
    }

   
    
    private AuthenticationProvider getAuthenticationProvider(org.kasource.web.websocket.config.xml.jaxb.AuthenticationProvider config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        AuthenticationProvider authProvider = null;
        String provider = config.getProvider();
        
        Class<?> providerClass = Class.forName(provider);
        authProvider = (AuthenticationProvider) providerClass.newInstance();
      
        if (authProvider == null) {
            authProvider = new PassthroughAutenticationProvider();
            
        }
        if(authProvider instanceof AbstractAuthenticationProvider){
            AbstractAuthenticationProvider authenticationProvider = (AbstractAuthenticationProvider) authProvider;
            authenticationProvider.setHeaderBased(config.isHeaderAuthentication());
            if(config.getUsernameKey() != null) {
                authenticationProvider.setUsernameKey(config.getUsernameKey());
            }
            if(config.getPasswordKey() != null) {
                authenticationProvider.setPasswordKey(config.getPasswordKey());
            }
        }
        return authProvider;
    }

}

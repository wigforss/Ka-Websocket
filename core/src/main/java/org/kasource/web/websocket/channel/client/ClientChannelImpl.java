package org.kasource.web.websocket.channel.client;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.server.HandshakeRequest;

import org.kasource.web.websocket.channel.ClientChannelListener;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract WebSocketManager with support for sending and receiving messages to and from multiple clients.
 * 
 * @author rikardwi
 **/
public class ClientChannelImpl implements ClientChannel {

    private static final Logger LOG = LoggerFactory.getLogger(ClientChannelImpl.class);
    
    private Map<String, ClientRegistration> clients = new ConcurrentHashMap<String, ClientRegistration>();
    private Map<String, Set<ClientRegistration>> clientsByUser = new ConcurrentHashMap<String, Set<ClientRegistration>>();

    private Set<ClientChannelListener> clientChannelListeners = new HashSet<ClientChannelListener>();
 
    private AuthenticationProvider authenticationProvider;


    /**
     * Add event listener.
     * 
     * @param webSocketEventListener the webSocketEventListener to add
     */
    public void addClientListener(ClientChannelListener listener) {
        this.clientChannelListeners.add(listener);
    }


    /**
     * Add the supplied client to the register and notifies listeners about this new client.
     * 
     * 
     * @param id                   ID of the client to add.
     * @param client               Client to add.
     * @param connectionParameters The connection parameters.
     **/
    @Override
    public void registerClient(WebSocketClient client) {
        ClientRegistration clientReg = new ClientRegistration(client);
        clients.put(client.getId(), clientReg);
        addClientForUser(clientReg);
        if (!clientChannelListeners.isEmpty()) {
            for (ClientChannelListener listener: clientChannelListeners) {
                listener.onConnect(client, clientReg.getParameterBinder());
            }
        }
    }
    
    
    private void addClientForUser(ClientRegistration clientReg) {
        if (clientReg.getClient().getUsername() != null) {
            Set<ClientRegistration> clientsForUser = clientsByUser.get(clientReg.getClient().getUsername());
            if(clientsForUser == null) {
                clientsForUser = new HashSet<ClientRegistration>();
                clientsByUser.put(clientReg.getClient().getUsername(), clientsForUser);
            }
            clientsForUser.add(clientReg);
        }
    }
    
    @Override
    public String authenticate(AuthenticationProvider provider, HandshakeRequest request) throws AuthenticationException {
        AuthenticationProvider auth = resolveAuthenticationProvider(provider);
        if (auth != null) {
            try {
                String username = auth.authenticate(request);
                fireAuthentication(username, request, null);
                return username;
            } catch(AuthenticationException ae) {
                LOG.warn("Unauthorized access by " + ae.getUsername() + " : " +ae.getMessage());
                fireAuthentication(ae.getUsername(), request, ae);
                throw ae;
            }    
            
        }
        return null;
       
    }
    
    private AuthenticationProvider resolveAuthenticationProvider(AuthenticationProvider provider) {
        if (provider != null) {
            return provider;
        } else {
            return authenticationProvider;
        }
    }
    
    
    private void fireAuthentication(String username, HandshakeRequest request, Throwable error) {
        if (!clientChannelListeners.isEmpty()) {
            for (ClientChannelListener listener: clientChannelListeners) {
                listener.onAuthentication(username, request, error);
            }
        }
    }
   
    /**
     * Remove client with id from the register and notifies listeners about the removed client.
     * 
     * @param id ID of the client to remove.
     */
    public void unregisterClient(WebSocketClient client) {
        ClientRegistration clientReg = clients.remove(client.getId());
        removeClientForUser(clientReg);
        if (!clientChannelListeners.isEmpty()) {
            for (ClientChannelListener listener: clientChannelListeners) {
                listener.onDisconnect(client, clientReg.getParameterBinder());
            }
        }
    }

    private void removeClientForUser(ClientRegistration clientReg) {
        if (clientReg != null && clientReg.getClient().getUsername() != null) {
            Set<ClientRegistration> clientsForUser = clientsByUser.get(clientReg.getClient().getUsername());
            if (clientsForUser != null) {
                clientsForUser.remove(clientReg);
            }
        }
    }
   
  
    
    /**
     * Notifies listeners on new message from a client.
     * 
     * @param message   Message from client
     * @param id        ID of the client
     */
    @Override
    public void onMessage(WebSocketClient client, String message) {
        ClientRegistration clientReg = clients.get(client.getId());
        if (!clientChannelListeners.isEmpty()) {
            for (ClientChannelListener listener: clientChannelListeners) {
                listener.onMessage(client, message, client.getTextProtocolHandler(), clientReg.getParameterBinder());
            }  
        }
    }
    
    /**
     * Notifies listeners on new message from a client.
     * 
     * @param message   Message from client
     * @param id        ID of the client
     */
    @Override
    public void onMessage(WebSocketClient client, byte[] message) {
      
        ClientRegistration clientReg = clients.get(client.getId());
        if (!clientChannelListeners.isEmpty()) {
            for (ClientChannelListener listener: clientChannelListeners) {
                listener.onBinaryMessage(client, message, client.getBinaryProtocolHandler(), clientReg.getParameterBinder());
            }
        }
    }
    
    
    
    /**
     * Returns true if this manager has a client registered with the supplied id.
     * @param id ID of the client to look for.
     * 
     * @return true if this manager has a client registered with the supplied id, else false.
     **/
     public boolean hasClient(String id) {
        return clients.containsKey(id);
    }

    /**
     * @param authenticationProvider the authenticationProvider to set
     */
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }



    @Override
    public void broadcast(Object message) {
        for (ClientRegistration clientReg : clients.values()) {
            try {
                clientReg.getClient().sendTextMessageToSocket(message);
            } catch (Exception e) {
                LOG.debug("Could not broadcast to " + clientReg.getClient().getUsername() + " with id " + clientReg.getClient().getId(), e);
            }
        }
        
    }

    @Override
    public void broadcastBinary(Object message) {
        for (ClientRegistration clientReg : clients.values()) {
            try {
                clientReg.getClient().sendBinaryMessageToSocket(message);
            } catch (Exception e) {
                LOG.debug("Could not broadcast to " + clientReg.getClient().getUsername() + " with id " + clientReg.getClient().getId(), e);
            }
       }
        
    }

    @Override
    public void sendMessageToUser(Object message, String username) throws IOException,
                NoSuchWebSocketClient {
        Set<ClientRegistration> clientsForUser = clientsByUser.get(username);
        if (clientsForUser == null || clientsForUser.isEmpty()) {
            throw new NoSuchWebSocketClient("No client found for user " + username);
        }
        for (ClientRegistration clientReg : clientsForUser) {
            clientReg.getClient().sendTextMessageToSocket(message);
        }
    }

    @Override
    public void sendMessageToClient(Object message, String clientId) throws IOException,
            NoSuchWebSocketClient {
        ClientRegistration clientReg = clients.get(clientId);
        if (clientReg == null) {
            throw new NoSuchWebSocketClient("No client found with ID " + clientId);
        }
        clientReg.getClient().sendTextMessageToSocket(message);     
    }
    
   
    
    @Override
    public void sendBinaryMessageToUser(Object message, String username) throws IOException,
                NoSuchWebSocketClient {
        Set<ClientRegistration> clientsForUser = clientsByUser.get(username);
        if (clientsForUser == null || clientsForUser.isEmpty()) {
            throw new NoSuchWebSocketClient("No client found for user " + username);
        }
        for (ClientRegistration clientReg : clientsForUser) {
            clientReg.getClient().sendBinaryMessageToSocket(message);
        }
        
    }
    
    @Override
    public void sendBinaryMessageToClient(Object message, String clientId) throws IOException,
                NoSuchWebSocketClient {
       
        ClientRegistration clientReg = clients.get(clientId);
        if(clientReg == null) {
            throw new NoSuchWebSocketClient("No client found with ID " + clientId);
        }
        clientReg.getClient().sendBinaryMessageToSocket(message);                 
    }
     
    
   

}

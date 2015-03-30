# Ka Websocket for DropWizard

## Add Web Socket support
Add the following dependencies to your project

       <dependency>
			<groupId>org.kasource.websocket</groupId>
			<artifactId>ka-websocket-dropwizard</artifactId>
			<version>0.4</version>
		</dependency>
			
		<dependency>
			<groupId>org.kasource.websocket</groupId>
			<artifactId>ka-websocket-jetty9</artifactId>
			<version>0.4</version>
			<scope>runtime</scope>
		</dependency>
 
 If you are running DropWizard 0.8.0 or later you need a jetty dependency for a newer version:
 	     
       <dependency>
			<groupId>org.kasource.websocket</groupId>
			<artifactId>ka-websocket-jetty93</artifactId>
			<version>0.4</version>
		</dependency>
		

## Create your Websocket POJO  
    
    @WebSocket("/chat/*")
    public class ChatServer {
	    @OnMessage
	    @Broadcast
	    public String recieveMessage(String message, String username) {
	        return username + " says: " + message;      
	    }
	    
	    @OnClientConnected
	    @Broadcast
	    public String onClientConnect(WebSocketClient client, String username) {
	        client.sendTextMessageToSocket("Welcome " + username);       
	        return username + " joined the conversation.";
	    }
	    
	    @OnClientDisconnected
	    @Broadcast
	    public String onClientDisconnect(String username) {
	       return username + " left the conversation.";
	    }
    }
    
## Run Initializer on start and register your POJO
Note that authentication is optional.

    public class ExampleApplication extends Application<ExampleConfiguration> {

        public static void main(String[] args) throws Exception {
            new ExampleApplication().run(args);
        }
    
        @Override
        public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
        }

        @Override
        public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
            Authenticator<BasicCredentials, User> authenticator = new ExampleAuthenticator();
            WebSocketInitializer<ExampleConfiguration> webSocketInitializer =
                    new WebSocketInitializer.Builder<ExampleConfiguration>(configuration, environment)
                        .basicAuthenticator(authenticator).build();
            environment.jersey().register(new PingResource()); 
            webSocketInitializer.onRun();
            webSocketInitializer.addWebocket(new ChatServer());
            environment.jersey().register(new BasicAuthProvider<User>(authenticator, "My Realm"));
      
    }
        
package org.kasource.websocket.tomcat8;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class ChatTest {

  
    @Test
    public void test() throws URISyntaxException {
        ChatClient chatClient = new ChatClient(new ChatClient.MessageHandler() {
            
            @Override
            public void handleMessage(String message) {
                System.out.println(message);
                
            }
        });
        
       chatClient.connect(new URI("ws://localhost:8080/chat-example/chat/channel1?username=test_user"));
       
        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
             
                e.printStackTrace();
            }
            chatClient.sendMessage("Hi There!!");
           
        }
    }
}

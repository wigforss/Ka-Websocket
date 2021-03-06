package org.kasource.web.websocket.config.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.WebsocketConfigXmlConfig;
public class WebSocketXmlConfigLoader implements WebSocketConfigLoader {

    private static final String CLASSPATH_RESOURCE_PREFIX = "classpath:";
    private static final String FILE_RESOURCE_PREFIX = "file:";
    private String configLocation;
    private ServletContext servletContext;
    
    public WebSocketXmlConfigLoader(String configLocation, ServletContext servletContext) {
        this.configLocation = configLocation;
        this.servletContext = servletContext;
    }
    
    @Override
    public WebSocketConfig loadConfig() {
        try {
            JAXBContext context = JAXBContext.newInstance(WebsocketConfigXmlConfig.class.getPackage().getName());
            if (configLocation.startsWith(FILE_RESOURCE_PREFIX)) {
                return loadConfigFromFile(context);
            } else if (configLocation.startsWith(CLASSPATH_RESOURCE_PREFIX)) {
                return loadConfigFromClasspath(context);
            } else {
                return loadConfigFromDefault(context);
            }
        } catch (JAXBException e) {
            throw new WebSocketConfigException("Could not configure WebSocket", e);
        }
    }

    private WebSocketConfig loadConfigFromFile(JAXBContext context) throws JAXBException {
        String location = configLocation;
        if (configLocation.startsWith(FILE_RESOURCE_PREFIX)) {
            location = configLocation.substring(FILE_RESOURCE_PREFIX.length());
        }
        WebsocketConfigXmlConfig configRoot = (WebsocketConfigXmlConfig) context.createUnmarshaller().unmarshal(new File(location));
        return new WebSocketXmlConfigBuilder().configure(configRoot, servletContext);
    }

    private WebSocketConfig loadConfigFromClasspath(JAXBContext context) throws JAXBException {
        String location = configLocation;
        if (configLocation.startsWith(CLASSPATH_RESOURCE_PREFIX)) {
            location = configLocation.substring(CLASSPATH_RESOURCE_PREFIX.length());
        }
        InputStream in = null;
        try {
            in = getResource(location);
            WebsocketConfigXmlConfig configRoot = (WebsocketConfigXmlConfig) context.createUnmarshaller().unmarshal(in);
            return new WebSocketXmlConfigBuilder().configure(configRoot, servletContext);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private InputStream getResource(String location) {
        InputStream in = servletContext.getResourceAsStream(location);
        if(in == null) {
            in = WebSocketXmlConfigLoader.class.getClassLoader().getResourceAsStream(location);
        }
        if(in == null) {
            throw new IllegalArgumentException("Could not locate resource " + location);
        }
        return in;
    }
    
    private WebSocketConfig loadConfigFromDefault(JAXBContext context) throws JAXBException {
        return loadConfigFromClasspath(context);
    }

    /**
     * @param configLocation
     *            the configLocation to set
     */
    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

}

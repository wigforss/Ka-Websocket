package org.kasource.web.websocket.client;

import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.HandshakeRequest;

public class HandshakeRequestData {
    private Map<String, List<String>> headers = new HashMap<String, List<String>>();
    private Map<String, List<String>> parameters = new HashMap<String, List<String>>();
    private URI requestUri;
    private HttpSession httpSession;
   // private Map<String, Object> sessionAttributes = new HashMap<String, Object>();
    
    public static HandshakeRequestData fromRequest(HandshakeRequest request) {
        HandshakeRequestData data = new HandshakeRequestData();
       
        data.setParameters(request.getParameterMap());
        data.setHeaders(request.getHeaders());
        data.setRequestUri(request.getRequestURI());
        if (request.getHttpSession() instanceof HttpSession) {
            data.setHttpSession((HttpSession) request.getHttpSession()); 
        }
        //data.initSession(request.getHttpSession());
        return data;
    }
    
   /*
    private void initSession(Object session) {
        if (session != null && session instanceof HttpSession) {
            HttpSession httpSession = (HttpSession) session;
            Enumeration<String> sessionAttributeNames = httpSession.getAttributeNames();
            while (sessionAttributeNames.hasMoreElements()) {
                String attribute = sessionAttributeNames.nextElement();
                sessionAttributes.put(attribute, httpSession.getAttribute(attribute));
            }
           
        }
    }
    */
    

    /**
     * @return the headers
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }


    /**
     * @return the parameters
     */
    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Map<String, List<String>> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the requestUri
     */
    public URI getRequestUri() {
        return requestUri;
    }

    /**
     * @param requestUri the requestUri to set
     */
    public void setRequestUri(URI requestUri) {
        this.requestUri = requestUri;
    }

    /**
     * @return the httpSession
     */
    public HttpSession getHttpSession() {
        return httpSession;
    }

    /**
     * @param httpSession the httpSession to set
     */
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
    
    public String getHeader(String headerName) {    
        List<String> headerValues = headers.get(headerName);
        if (headerValues != null && !headerValues.isEmpty()) {
            return headerValues.get(0);
        }
        return null;       
    }
    
    public String getParameter(String parameterName) {
        List<String> parametersValues = parameters.get(parameterName);
        if (parametersValues != null && !parametersValues.isEmpty()) {
            return parametersValues.get(0);
        } 
        return null;
    }
}

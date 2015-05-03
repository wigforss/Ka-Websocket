package org.kasource.web.websocket.servlet;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

public class HttpServletHandshakeRequest implements HandshakeRequest {
    private HttpServletRequest request;
    private Map<String, List<String>> headers = new HashMap<String, List<String>>();
    private Map<String, List<String>> parameterMap = new HashMap<String, List<String>>();
    
    public HttpServletHandshakeRequest(HttpServletRequest request) {
        this.request = request;
        initHeaders();
        initParameterMap();
    }
    
    private void initHeaders() {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> valueEnumeration = request.getHeaders(headerName);
            List<String> values = new ArrayList<String>();
            while (valueEnumeration.hasMoreElements()) {
                values.add(valueEnumeration.nextElement());
            }
            headers.put(headerName, values);
            
        }
    }
    
    private void initParameterMap() {
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            parameterMap.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    @Override
    public URI getRequestURI() {
       return URI.create(request.getRequestURI());
    }

    @Override
    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    @Override
    public Object getHttpSession() {
       
        return request.getSession();
    }

    @Override
    public Map<String, List<String>> getParameterMap() {
        return parameterMap;
    }

    @Override
    public String getQueryString() {
        return request.getQueryString();
    }
}

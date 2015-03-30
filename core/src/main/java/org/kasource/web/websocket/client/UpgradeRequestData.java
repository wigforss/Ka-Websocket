package org.kasource.web.websocket.client;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class UpgradeRequestData {
    private Map<String, String> headers = new HashMap<String, String>();
    private String ipAddress;
    private Map<String, String[]> parameters = new HashMap<String, String[]>();
    private String requestUri;
    
    public static UpgradeRequestData fromRequest(HttpServletRequest request) {
        UpgradeRequestData data = new UpgradeRequestData();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            data.getHeaders().put(headerName, request.getHeader(headerName));
            
        }
        data.setParameters(request.getParameterMap());
        data.setIpAddress(request.getRemoteAddr());
        data.setRequestUri(request.getRequestURI());
        return data;
    }

    /**
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the parameters
     */
    public Map<String, String[]> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the requestUri
     */
    public String getRequestUri() {
        return requestUri;
    }

    /**
     * @param requestUri the requestUri to set
     */
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }
}

package org.kasource.web.websocket.impl;

import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.kasource.web.websocket.util.HeaderLookup;

public class UpgradeRequestHeaderLookup implements HeaderLookup {

    private UpgradeRequest request;
    
    public UpgradeRequestHeaderLookup(UpgradeRequest request) {
        this.request = request;
    }
    
    @Override
    public String getHeaderValue(String header) {
       return request.getHeader(header);
    }

}

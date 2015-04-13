package org.waastad.jca.acme.outbound.api;

import javax.resource.ResourceException;

public interface TradeConnectionFactory {

    public TradeConnection getConnection() throws ResourceException;
    
}

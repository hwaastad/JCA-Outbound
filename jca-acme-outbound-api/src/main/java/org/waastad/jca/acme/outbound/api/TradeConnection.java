package org.waastad.jca.acme.outbound.api;

import javax.resource.ResourceException;

public interface TradeConnection {

    public TradeResponse submitOrder(TradeOrder order)
            throws TradeProcessingException;

    public void close() throws ResourceException;

}

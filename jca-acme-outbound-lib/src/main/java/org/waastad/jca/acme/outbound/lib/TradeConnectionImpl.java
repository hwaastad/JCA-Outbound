
package org.waastad.jca.acme.outbound.lib;

import java.io.IOException;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import org.waastad.jca.acme.outbound.api.TradeConnection;
import org.waastad.jca.acme.outbound.api.TradeOrder;
import org.waastad.jca.acme.outbound.api.TradeProcessingException;
import org.waastad.jca.acme.outbound.api.TradeResponse;


public class TradeConnectionImpl implements TradeConnection {

    private static final Logger log = Logger.getLogger("TradeConnectionImpl");
    private TradeManagedConnection mconnection;
    private boolean valid;

    TradeConnectionImpl(TradeManagedConnection mconnection) {
        this.mconnection = mconnection;
        valid = true;
    }

    TradeManagedConnection getManagedConnection() {
        return mconnection;
    }

    void setManagedConnection(TradeManagedConnection mconnection) {
        this.mconnection = mconnection;
    }

    void invalidate() {
        valid = false;
    }


    @Override
    public TradeResponse submitOrder(TradeOrder order) throws TradeProcessingException {
        log.info("[TradeConnectionImpl] submitOrder()");
        if (valid) {
            try {
                String resp = mconnection.sendCommandToEIS(order.toString());
                return new TradeResponse();
            } catch (IOException e) {
                throw new TradeProcessingException(e.getMessage());
            }
        } else {
            throw new TradeProcessingException("Connection handle is invalid");
        }
    }

    @Override
    public void close() throws ResourceException {
        log.info("[TradeConnectionImpl] close()");
        valid = false;
        mconnection.disassociateConnection();
    }

}

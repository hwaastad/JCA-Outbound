package org.waastad.jca.acme.outbound.lib;

import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import org.waastad.jca.acme.outbound.api.TradeConnection;
import org.waastad.jca.acme.outbound.api.TradeConnectionFactory;

public class TradeConnectionFactoryImpl implements TradeConnectionFactory {

    private static final Logger log = Logger.getLogger("TradeConnectionFactoryImpl");
    private ConnectionManager cmanager;
    private TradeManagedConnectionFactory mcfactory;
    
    TradeConnectionFactoryImpl(TradeManagedConnectionFactory mcfactory,
                               ConnectionManager cmanager) {
        this.mcfactory = mcfactory;
        this.cmanager = cmanager;
    }

    @Override
    public TradeConnection getConnection() throws ResourceException {
        log.info("[TradeConnectionFactoryImpl] getConnection()");
        return (TradeConnection) cmanager.allocateConnection(mcfactory, null);
    }
}

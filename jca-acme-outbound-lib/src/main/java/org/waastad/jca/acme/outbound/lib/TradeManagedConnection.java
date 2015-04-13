
package org.waastad.jca.acme.outbound.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

public class TradeManagedConnection implements ManagedConnection {

    private static final Logger log = Logger.getLogger("TradeManagedConnection");
       
    private TradeConnectionImpl connection;
    private List<TradeConnectionImpl> createdConnections;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private PrintWriter logwriter;
    

    TradeManagedConnection(String host, String port) throws IOException {
        
        log.info("[TradeManagedConnection] Constructor");
        createdConnections = new ArrayList<>();
        
        /* EIS-specific procedure to obtain a new connection */
        int portnum = Integer.parseInt(port);
        log.info(String.format("Connecting to %s on port %s...", host, port));
        socket = new Socket(host, portnum);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        /* Skip greeting */
        in.readLine(); in.readLine();
        log.info("Connected!");
    }
    
    String sendCommandToEIS(String command) throws IOException {
        out.println(command);
        return in.readLine();
    }
    

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cinfo) 
                                throws ResourceException {

        log.info("[TradeManagedConnection] getConnection()");
        connection = new TradeConnectionImpl(this);
        return connection;
    }

    @Override
    public void destroy() throws ResourceException {
        try {
            log.info("[TradeManagedConnection] destroy()");
            socket.close();
        } catch (IOException e) {}
        
    }

    @Override
    public void cleanup() throws ResourceException { 
        log.info("[TradeManagedConnection] cleanup()");
        for (TradeConnectionImpl con : createdConnections)
            if (con != null)
                con.invalidate();
    }

    @Override
    public void associateConnection(Object connection) throws ResourceException {
        log.info("[TradeManagedConnection] associateConnection()");
        this.connection = (TradeConnectionImpl) connection;
        this.connection.setManagedConnection(this);
    }
    
    public void disassociateConnection() {
        this.connection = null;
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {}

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {}

    @Override
    public XAResource getXAResource() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new ManagedConnectionMetaData() {
            @Override
            public String getEISProductName() throws ResourceException {
                return "MegaTrade Execution Platform";
            }
            @Override
            public String getEISProductVersion() throws ResourceException {
                return "7.0";
            }
            @Override
            public int getMaxConnections() throws ResourceException {
                return 0;
            }
            @Override
            public String getUserName() throws ResourceException {
                return "defaultUser";
            }
        };
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logwriter = out;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return logwriter;
    }   
}

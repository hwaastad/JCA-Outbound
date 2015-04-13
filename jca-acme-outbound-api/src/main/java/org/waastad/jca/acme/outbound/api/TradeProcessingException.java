package org.waastad.jca.acme.outbound.api;

public class TradeProcessingException extends Exception {

    private static final long serialVersionUID = -2535071618451027587L;

    public TradeProcessingException(String msg) {
        super(msg);
    }
}

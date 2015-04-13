package org.waastad.jca.acme.outbound.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.resource.ResourceException;
import org.waastad.jca.acme.outbound.api.TradeConnection;
import org.waastad.jca.acme.outbound.api.TradeConnectionFactory;

@Singleton
public class AnotherSchedulerBean {

//    @Resource(name = "jca-acme-outbound-ra-1.0-SNAPSHOT")
    @Resource(name = "EISResourceConnectionFactory")
    private TradeConnectionFactory anotherConnectionFactory;

    @Schedule(hour = "*", minute = "*", second = "*/12")
    public void doStuff() {
        System.out.println("Trying to get connection.....");
        try {
            System.out.println("Trying to get connection.....");
            TradeConnection connection = anotherConnectionFactory.getConnection();
        } catch (ResourceException ex) {
            Logger.getLogger(AnotherSchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

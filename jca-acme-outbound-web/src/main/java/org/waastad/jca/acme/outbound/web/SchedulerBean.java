
package org.waastad.jca.acme.outbound.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.resource.ResourceException;
import org.waastad.jca.acme.outbound.api.TradeConnection;
import org.waastad.jca.acme.outbound.api.TradeConnectionFactory;


@Singleton
//@Startup
public class SchedulerBean {

//    @Resource(name = "jca-acme-outbound-ra-1.0-SNAPSHOT")
    @Resource(name = "EISResourceConnectionFactory")
    private TradeConnectionFactory connectionFactory;

    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void doStuff() {
        System.out.println("Trying to get connection.....");
        try {
            System.out.println("Trying to get connection.....");
            TradeConnection connection = connectionFactory.getConnection();
        } catch (ResourceException ex) {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

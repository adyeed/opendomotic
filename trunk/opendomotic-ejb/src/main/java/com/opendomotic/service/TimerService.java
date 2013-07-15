/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author jaques
 */
@Singleton
public class TimerService {

    private static final Logger LOG = Logger.getLogger(TimerService.class.getName());
    private BroadcastWebsocket broadcastWebsocket;
    
    @Inject
    private JobService jobService;
    
    @Schedule(minute = "*/1", hour = "*")
    public void doWork() {       
        //LOG.info("Timer trigger");
        jobService.checkJobs();
        
        if (broadcastWebsocket != null) {
            broadcastWebsocket.sendBroadcast(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        }
    }

    public void setBroadcastWebsocket(BroadcastWebsocket broadcastWebsocket) {
        this.broadcastWebsocket = broadcastWebsocket;
    }

}

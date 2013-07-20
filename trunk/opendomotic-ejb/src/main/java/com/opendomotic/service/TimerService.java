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
    
    @Inject
    private JobService jobService;
    
    @Inject
    private WebSocketService webSocketService;
    
    @Schedule(minute = "*/1", hour = "*")
    public void doWork() {       
        //LOG.info("Timer trigger");
        jobService.checkJobs();
        
        webSocketService.send(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
    }

}

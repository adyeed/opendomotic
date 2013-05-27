/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author jaques
 */
@Singleton
public class TimerService {

    private static final Logger LOG = Logger.getLogger(TimerService.class.getName());

    @EJB
    private JobService jobService;
    
    @Schedule(minute = "*/1", hour = "*")
    public void doWork() {       
        //LOG.info("Timer trigger");
        jobService.checkJobs();
    }
    
}

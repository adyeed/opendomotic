/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.api.Device;
import com.opendomotic.service.websocket.WebSocketService;
import com.opendomotic.model.entity.Job;
import com.opendomotic.service.dao.JobDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author jaques
 */
@Singleton
public class JobService {
    
    private static final Logger LOG = Logger.getLogger(JobService.class.getName());
    
    @Inject
    private DeviceService deviceService;
    
    @Inject
    private WebSocketService webSocketService;
    
    @Inject
    private JobDAO jobDAO;
    
    @Schedule(minute = "*/1", hour = "*")
    public void timerJobs() {       
        LOG.info("Timer trigger");
        checkJobs();        
        webSocketService.send(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
    }
    
    public void checkJobs() {
        Date now = new Date();
        
        for (Job job : jobDAO.findAll()) {
            LOG.log(Level.INFO, "checking job: {0}", job.toString());
            boolean canChange = false;
            
            if (job.getInput() != null) {
                Device input = deviceService.getDevice(job.getInput().getName());                
                switch (job.getOperator()) {
                    case EQUAL:             canChange = input.getValue().equals(job.getExpectValueAsInt()); break;
                    case DIFERENT:          canChange = !input.getValue().equals(job.getExpectValueAsInt()); break;
                    case GREATHER:          canChange = (Integer) input.getValue() >  job.getExpectValueAsInt(); break;
                    case GREATHER_EQUAL:    canChange = (Integer) input.getValue() >= job.getExpectValueAsInt(); break;
                    case LESS:              canChange = (Integer) input.getValue() <  job.getExpectValueAsInt(); break;
                    case LESS_EQUAL:        canChange = (Integer) input.getValue() <= job.getExpectValueAsInt(); break;
                }
            } else if (job.getInputDate() != null && job.getInputDate().compareTo(now) <= 0) {
                canChange = true;
            }
            
            if (canChange) {
                LOG.info("running job!");
                
                deviceService.setDeviceValue(
                        job.getOutput().getName(), 
                        job.getActionValueAsInt());
                
                if (job.isDeleteAfterExecute()) {
                    jobDAO.delete(job);
                }
            }
        }
    }
    
}

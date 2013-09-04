package com.opendomotic.service;

import com.opendomotic.model.entity.Job;
import com.opendomotic.service.dao.JobDAO;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author jaques
 */
@Singleton
@Startup
public class JobService {
    
    private static final Logger LOG = Logger.getLogger(JobService.class.getName());
    
    @Inject
    private DeviceService deviceService;
    
    @Inject
    private JobDAO jobDAO;
        
    @Schedule(minute = "*/1", hour = "*")
    public void timerJobs() {
        if (deviceService.isScheduleInitialized()) { //to avoid ConcurrentAccessTimeoutException:
            if (checkExecuteJobs()) {
                deviceService.updateDeviceValuesAsync();
            }
        }
    }
    
    private boolean checkExecuteJobs() {
        boolean executed = false;
        Date now = new Date();
        
        for (Job job : jobDAO.findAll()) {
            //LOG.log(Level.INFO, "checking job: {0}", job.toString());
            boolean canExecute = false;
            
            if (job.getInput() != null) {
                Object inputValue = deviceService.getDeviceValue(job.getInput().getName());  
                if (inputValue != null) {
                    switch (job.getOperator()) { //TO-DO: Actually run only with integers. Add another types.
                        case EQUAL:             canExecute = inputValue.equals(job.getExpectValueAsInt()); break;
                        case DIFERENT:          canExecute = !inputValue.equals(job.getExpectValueAsInt()); break;
                        case GREATHER:          canExecute = (Integer) inputValue >  job.getExpectValueAsInt(); break;
                        case GREATHER_EQUAL:    canExecute = (Integer) inputValue >= job.getExpectValueAsInt(); break;
                        case LESS:              canExecute = (Integer) inputValue <  job.getExpectValueAsInt(); break;
                        case LESS_EQUAL:        canExecute = (Integer) inputValue <= job.getExpectValueAsInt(); break;
                    }
                }
            } else if (job.getInputDate() != null && job.getInputDate().compareTo(now) <= 0) {
                canExecute = true;
            }
            
            if (canExecute) {
                Object outputValue = deviceService.getDeviceValue(job.getOutput().getName()); 
                if (outputValue != null && !outputValue.equals(job.getActionValueAsInt())) {
                    LOG.log(Level.INFO, "executing job: {0}", job.toString());

                    deviceService.setDeviceValue(
                            job.getOutput().getName(), 
                            job.getActionValueAsInt());

                    //TO-DO: Check if really setted the value in device. Can occurs communication error.
                    if (job.isDeleteAfterExecute()) {
                        jobDAO.delete(job);
                    }
                    executed = true;
                }
            }
        }
        return executed;
    }
    
}

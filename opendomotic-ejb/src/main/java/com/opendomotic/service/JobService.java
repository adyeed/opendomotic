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
        
        for (Job job : jobDAO.findAllEnabled()) {
            try {
                //LOG.log(Level.INFO, "Checking job: {0}", job.toString());
                if (canExecuteJob(job, now) && executeJob(job)) {
                    executed = true;
                }
            } catch (Exception ex) {
                LOG.log(Level.INFO, "Error executing job: {0} | {1}", new Object[] {job.toString(), ex.toString()});
            }
        }
        return executed;
    }
    
    private boolean canExecuteJob(Job job, Date now) throws Exception {
        if (job.getInput() != null) {
            Object inputValue = deviceService.getDeviceValue(job.getInput().getName());
            if (inputValue != null) {
                if (!(inputValue instanceof Comparable)) {
                    throw new Exception("Device.value must be a Comparable instance");
                }
                Comparable input = (Comparable) inputValue;
                Object expectValue = job.getExpectValueAsType(inputValue.getClass());
                return job.getOperator().compare(input, expectValue);
            }
        } else if (job.getInputDate() != null && job.getInputDate().compareTo(now) <= 0) {
            return true;
        }
        return false;
    }
    
    private boolean executeJob(Job job) {
        Object outputValue = deviceService.getDeviceValue(job.getOutput().getName());
        if (outputValue != null) {
            Object actionValue = job.getActionValueAsType(outputValue.getClass());
            if (!outputValue.equals(actionValue)) {
                LOG.log(Level.INFO, "Executing job: {0}", job.toString());

                deviceService.setDeviceValue(
                        job.getOutput().getName(), 
                        actionValue);

                //TO-DO: Check if really changed the value in device. Can occurs communication error.
                if (job.isDeleteAfterExecute()) {
                    jobDAO.delete(job);
                }
                return true;
            }
        }
        return false;
    }
    
}

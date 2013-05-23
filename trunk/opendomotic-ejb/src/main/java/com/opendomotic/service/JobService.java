/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.model.Job;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Singleton;

/**
 *
 * @author jaques
 */
@Singleton
public class JobService {
    
    private static final Logger LOG = Logger.getLogger(JobService.class.getName());
    
    private List<Job> listJob = new ArrayList<>();
    
    public void addJob(Job job) {
        listJob.add(job);
    }
    
    public void removeJob(Job job) {
        listJob.remove(job);
    }
    
    public void checkJobs() {
        Date now = new Date();
        int index = 0;
        while (index < listJob.size()) {
            Job job = listJob.get(index);
            if (job.getDate().compareTo(now) <= 0) {
                String log = "Running job: "+job.toString();
                LOG.info(log);
                job.getDevice().setValue(job.getValue());
                listJob.remove(index);
            } else {
                index++;
            }
        }
    }

    public List<Job> getListJob() {
        return listJob;
    }
    
}

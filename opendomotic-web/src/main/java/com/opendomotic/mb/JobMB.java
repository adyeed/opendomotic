/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.Job;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.JobService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author jaques
 */
@ManagedBean
public class JobMB {
    
    @EJB
    private DeviceService deviceService;
    
    @EJB
    private JobService jobService;
    
    private String date;
    private String deviceName;
    private String value;
    
    private Job job = new Job();
    
    public List<Job> getListJob() {
        return jobService.getListJob();
    }
    
    public void addJob() throws ParseException {   
        job.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date));
        job.setDevice(deviceService.getDevice(deviceName));
        job.setValue(Integer.parseInt(value));
        jobService.addJob(job);
    }
    
    public void removeJob(Job job) {
        jobService.removeJob(job);
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}

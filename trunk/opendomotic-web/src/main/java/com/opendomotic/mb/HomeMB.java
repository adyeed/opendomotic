/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.EnvironmentDAO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class HomeMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(HomeMB.class.getName());
    
    @Inject
    private EnvironmentDAO environmentDAO;
    
    @Inject
    private DeviceService deviceService;
    
    private Integer idEnvironment;
    
    @PostConstruct
    public void init() {
        idEnvironment = environmentDAO.findFirst().getId();
        LOG.log(Level.INFO, "HomeMB init id={0}", idEnvironment);
    }
    
    public void updateDevices() {
        deviceService.updateDeviceValuesAsync();
    }
    
    public Integer getIdEnvironment() {
        return idEnvironment;
    }

    public void setIdEnvironment(Integer idEnvironment) {
        this.idEnvironment = idEnvironment;
    }
    
    public String getTemperaturaMin() {
        if (deviceService.getTemperaturaMinValue() != null) {        
            return deviceService.getTemperaturaMinValue() + "°C | " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(deviceService.getTemperaturaMinDate());
        } else {
            return "";
        }
    }
    
}

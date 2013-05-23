/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceFactory;
import com.opendomotic.model.DeviceProxy;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author jaques
 */
@Startup
@Singleton
public class DeviceService {
    
    private static final Logger LOG = Logger.getLogger(DeviceService.class.getName());
    
    private Map<String, DeviceProxy> mapDevice;
    private List<DeviceProxy> listDevice;
    
    @PostConstruct
    public void init() {
        LOG.info("DeviceService init...");
        
        DeviceFactory factory = new DeviceFactory();
        factory.createDevices();
        mapDevice = factory.getMapDevice();
        listDevice = factory.getListDevice();
    }
    
    public Device getDevice(String name) {
        return mapDevice.get(name);
    }
    
    public void updateDevices() {
        for (DeviceProxy device : listDevice) {
            device.updateValue();
        }
    }
    
    public List<DeviceProxy> getListDevice() {
        return listDevice;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DeviceConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author jaques
 */
@Startup
@Singleton
public class DeviceService {
    
    private static final Logger LOG = Logger.getLogger(DeviceService.class.getName());
    
    private Map<String, DeviceProxy> mapDevice;
    
    @Inject
    private DeviceConfigService deviceConfigService;
    
    @PostConstruct
    public void init() {    
        loadDevices();
    }
    
    public void loadDevices() {
        LOG.info("loading devices..."); 
        
        mapDevice = new HashMap<>();            
        for (DeviceConfig config : deviceConfigService.findAll()) {
            Device device = config.createDevice();
            if (device != null) {
                mapDevice.put(device.getName(), new DeviceProxy(device));
            }
        }
    }
    
    public void updateDevices() {
        for (DeviceProxy device : mapDevice.values()) {
            device.updateValue();
        }
    }
    
    public Device getDevice(String name) {
        return mapDevice.get(name);
    }

    public Map<String, DeviceProxy> getMapDevice() {
        return mapDevice;
    }
    
    public List<DeviceProxy> createListDevice() {
        List<DeviceProxy> list = new ArrayList();
        for (DeviceProxy device : mapDevice.values()) {
            list.add(device);
        }
        return list;
    }
    
}

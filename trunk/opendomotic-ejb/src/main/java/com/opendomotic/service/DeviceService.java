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
    
    private Map<String, DeviceProxy> mapDevice = new HashMap<>();
    private List<DeviceProxy> listDevice = new ArrayList<>(); //contém a lista por causa da iteração do jsf que não funciona com map
    
    @Inject
    private DeviceConfigService deviceConfigService;
    
    @PostConstruct
    public void init() {
        LOG.info("DeviceService init...");        
        loadDevices();
    }
    
    private void loadDevices() {
        System.out.println(deviceConfigService);
        for (DeviceConfig config : deviceConfigService.findAll()) {
            Device device = config.createDevice();
            if (device != null) {
                addDevice(device);
            }
        }
    }
    
    public void addDevice(Device device) {
        DeviceProxy deviceProxy = new DeviceProxy(device);
        mapDevice.put(device.getName(), deviceProxy);
        listDevice.add(deviceProxy);
    } 
    
    public void removeDevice(String name) {
        mapDevice.remove(name);
        
        for (DeviceProxy deviceProxy : listDevice) {
            if (deviceProxy.getName().equals(name)) {
                listDevice.remove(deviceProxy);
                break;
            }
        }
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

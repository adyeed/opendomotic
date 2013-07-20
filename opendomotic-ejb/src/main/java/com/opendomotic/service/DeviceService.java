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
import javax.ejb.Schedule;
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

    @Inject
    private WebSocketService webSocketService;
    
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

    @Schedule(second = "*/30", minute = "*", hour = "*")
    public void executeUpdate() {
        updateDevices();
    }

    public void updateDevices() {
        boolean changed = false;
        for (DeviceProxy device : mapDevice.values()) {
            if (device.updateValue()) {
                changed = true;
            }
        }
        
        if (changed) {
            //TO-DO: se alterou estado, notificar apenas os devices correspondentes:
            webSocketService.send("update");
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

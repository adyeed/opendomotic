/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.service.websocket.WebSocketService;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.device.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DeviceConfig;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
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
@Lock(LockType.WRITE)
@AccessTimeout(value = 10, unit = TimeUnit.SECONDS)
public class DeviceService {

    private static final Logger LOG = Logger.getLogger(DeviceService.class.getName());
    private Map<String, DeviceProxy> mapDevice;
    
    @Inject
    private DeviceConfigDAO deviceConfigService;

    @Inject
    private WebSocketService webSocketService;
       
    @PostConstruct
    public void init() {
        loadDevices();
    }
    
    public void loadDevices() {
        LOG.info("loading devices...");

        mapDevice = Collections.synchronizedMap(new LinkedHashMap<String, DeviceProxy>());
        for (DeviceConfig config : deviceConfigService.findAll()) {
            try {
                LOG.log(Level.INFO, "creating {0}", config.getName());
                Device device = config.createDevice();
                if (device != null) {
                    mapDevice.put(config.getName(), new DeviceProxy(device));
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error on create device: {0}", ex.toString());
            }
        }
    }

    @Schedule(second = "*/30", minute = "*", hour = "*")
    public void updateDeviceValuesTimer() {
        updateDeviceValues("timer", false);
    }
    
    @Asynchronous
    public void updateDeviceValuesAsync(boolean alwaysSendWebsocket) {
        updateDeviceValues("async", alwaysSendWebsocket);
    }
    
    private void updateDeviceValues(String origin, boolean alwaysSendWebsocket) {
        long millisTotal = System.currentTimeMillis();
        
        boolean changed = false;
        for (Entry<String, DeviceProxy> entry : mapDevice.entrySet()) {
            DeviceProxy device = entry.getValue();
            long millisDevice = System.currentTimeMillis();
            if (device.updateValue()) {              
                changed = true;
            }
            logTime(device.toString(), millisDevice, 1000);
        }
        
        if (changed || alwaysSendWebsocket) {
            //TO-DO: se alterou estado, notificar apenas os devices correspondentes:
            webSocketService.sendUpdateDeviceValues(origin);
        }
        
        logTime("Total", millisTotal, 2000);
    }
    
    public void setDeviceValue(String deviceName, Object value) {
        Device device = mapDevice.get(deviceName);
        if (device != null) {
            device.setValue(value);
        }
    }
    
    @Lock(LockType.READ)
    public Object getDeviceValue(String deviceName) {
        Device device = mapDevice.get(deviceName);
        if (device != null) {
            return device.getValue();
        } else {
            return null;
        }
    }
    
    public Object toggleDeviceValue(String deviceName) {
        Device device = mapDevice.get(deviceName);
        if (device != null) {
            Object value = device.getValue() == 1 ? 0 : 1;
            device.setValue(value);
            return value;
        } else {
            return null;
        }
    }
    
    private void logTime(String item, long startMillis, int limit) {
        long elapsedMillis = System.currentTimeMillis() - startMillis;
        if (elapsedMillis > limit) {
            LOG.log(Level.WARNING, "Slow reading device: {0} ms | {1}", new Object[] {elapsedMillis, item});
        }
    }
    
}

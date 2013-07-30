/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.service.websocket.WebSocketService;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DeviceConfig;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author jaques
 */
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
@Startup
public class DeviceService {

    private static final Logger LOG = Logger.getLogger(DeviceService.class.getName());
    private Map<String, DeviceProxy> mapDevice;
    
    @Inject
    private DeviceConfigDAO deviceConfigService;

    @Inject
    private WebSocketService webSocketService;
    
    //temporário:
    private Integer temperaturaMinValue;
    private Date temperaturaMinDate;
    
    @PostConstruct
    public void init() {
        loadDevices();
    }

    public void loadDevices() {
        LOG.info("loading devices...");

        mapDevice = new LinkedHashMap<>();
        for (DeviceConfig config : deviceConfigService.findAll(new String[] {"protocol","address","param","name"})) {
            try {
                Device device = config.createDevice();
                if (device != null) {
                    mapDevice.put(device.getName(), new DeviceProxy(device));
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error on create device: {0}", ex.toString());
            }
        }
    }

    @Schedule(second = "*/30", minute = "*", hour = "*")
    public void updateDeviceValuesTimer() {
        updateDeviceValues("timer");
    }
    
    @Asynchronous
    public void updateDeviceValuesAsync() {
        updateDeviceValues("async");
    }
    
    @Lock
    public void updateDeviceValues(String origin) {
        boolean changed = false;
        for (DeviceProxy device : mapDevice.values()) {
            if (device.updateValue()) {
                checkTemperaturaMin(device);                
                changed = true;
            }
        }
        
        if (changed) {
            //TO-DO: se alterou estado, notificar apenas os devices correspondentes:
            webSocketService.sendUpdateDeviceValues(origin);
        }
    }
    
    @Lock
    public void toggleDeviceValue(Device device) {
        device.setValue(device.getValue() == 1 ? 0 : 1);    
    }
    
    @Lock
    public void setDeviceValue(String deviceName, Object value) {
        Device device = getDevice(deviceName);
        if (device != null) {
            device.setValue(value);
        }
    }
    
    //temporário:
    private void checkTemperaturaMin(DeviceProxy device) {
        if (device.getName().equals("Temperatura") && device.getValue() != null) {
            Integer temperatura = (Integer) device.getValue();
            if (temperaturaMinValue == null || temperatura < temperaturaMinValue) {
                temperaturaMinValue = temperatura;
                temperaturaMinDate = new Date();
            }
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

    public Integer getTemperaturaMinValue() {
        return temperaturaMinValue;
    }

    public Date getTemperaturaMinDate() {
        return temperaturaMinDate;
    }
    
}

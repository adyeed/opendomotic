/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DeviceConfig;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
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

        mapDevice = new HashMap<>();
        for (DeviceConfig config : deviceConfigService.findAll()) {
            Device device = config.createDevice();
            if (device != null) {
                mapDevice.put(device.getName(), new DeviceProxy(device));
            }
        }
    }

    @Schedule(second = "*/30", minute = "*", hour = "*")
    public void timerUpdateDeviceValues() {
        updateDeviceValues();
    }
    
    public void updateDeviceValues() {
        boolean changed = false;
        for (DeviceProxy device : mapDevice.values()) {
            if (device.updateValue()) {
                checkTemperaturaMin(device);                
                changed = true;
            }
        }
        
        if (changed) {
            //TO-DO: se alterou estado, notificar apenas os devices correspondentes:
            webSocketService.sendUpdateDeviceValues("all");
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
    
    @Asynchronous
    public void toggleDeviceValue(Device device) {
        device.setValue(device.getValue() == 1 ? 0 : 1);
        
        //já envia a atualizacao do device pq a leitura de todos os outros é demorada.
        webSocketService.sendUpdateDeviceValues("toggle " + device.getName()); //TO-DO: Enviar com idPosition? Não precisa atualizar todos.
        
        //atualiza pq toggle pode afetar outros devices:
        //se habilitar, travará o DeviceRestService.getListValue. Usar jms?
        //updateDeviceValues();
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.rest.GraphicDevice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    
    private Map<String, DeviceProxy> mapDevice = new HashMap<>();
    private List<DeviceProxy> listDevice = new ArrayList<>(); //contém a lista por causa da iteração do jsf que não funciona com map
    private List<GraphicDevice> listGraphicDevice;
    
    @EJB
    private DeviceConfigService deviceConfigService;
    
    @PostConstruct
    public void init() {
        LOG.info("DeviceService init...");        
        listGraphicDevice = createListGraphicDevice();
        loadDevices();
    }
    
    private void loadDevices() {
        for (DeviceConfig config : deviceConfigService.getListAll()) {
            addDevice(config.createDevice());
        }
    }
    
    public void addDevice(Device device) {
        DeviceProxy deviceProxy = new DeviceProxy(device);
        mapDevice.put(device.getName(), deviceProxy);
        listDevice.add(deviceProxy);
    } 
    
    public void removeDevice(String name) {
        //listDevice.remove(mapDevice.get(device.getName()));
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

    public List<GraphicDevice> getListGraphicDevice() {
        return listGraphicDevice;
    }

    private List<GraphicDevice> createListGraphicDevice() {
        List<GraphicDevice> list = new ArrayList<>();
        /*
        //quartos:
        list.add(new GraphicDevice(1, 820, 170, "Teste ethernet", "../resources/images/lampada-off.png", "../resources/images/lampada-on.png"));
        list.add(new GraphicDevice(2, 710, 480, "Luz escritorio", "../resources/images/lampada-off.png", "../resources/images/lampada-on.png"));

        //sala:
        list.add(new GraphicDevice(4, 300, 400, "Luz sala", "../resources/images/lampada-off.png", "../resources/images/lampada-on.png"));

        //sacada:
        list.add(new GraphicDevice(6, 150, 150, "Temperatura DHT11", "../resources/images/termometro.png", null));
        list.add(new GraphicDevice(7, 250, 150, "Umidade", "../resources/images/umidade.png", null));
        */
        return list;
    }
    
}

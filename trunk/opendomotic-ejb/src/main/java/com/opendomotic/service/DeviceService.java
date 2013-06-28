/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceFactory;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.rest.GraphicDevice;
import java.util.ArrayList;
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
    private List<GraphicDevice> listGraphicDevice;
    
    @PostConstruct
    public void init() {
        LOG.info("DeviceService init...");
        
        listGraphicDevice = createListGraphicDevice();
        
        DeviceFactory factory = new DeviceFactory();
        factory.createDevices();
        mapDevice = factory.getMapDevice();
        listDevice = factory.getListDevice();
    }
    
    private List<GraphicDevice> createListGraphicDevice() {
        List<GraphicDevice> list = new ArrayList<>();
        
        //quartos:
        list.add(new GraphicDevice(1, 820, 170, "Teste ethernet", "../resources/images/lampada-off.png", "../resources/images/lampada-on.png"));
        list.add(new GraphicDevice(2, 710, 480, "Luz escritorio", "../resources/images/lampada-off.png", "../resources/images/lampada-on.png"));

        //sala:
        list.add(new GraphicDevice(4, 300, 400, "Luz sala", "../resources/images/lampada-off.png", "../resources/images/lampada-on.png"));

        //sacada:
        list.add(new GraphicDevice(6, 150, 150, "Temperatura DHT11", "../resources/images/termometro.png", null));
        list.add(new GraphicDevice(7, 250, 150, "Umidade", "../resources/images/umidade.png", null));
        
        return list;
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
    
}

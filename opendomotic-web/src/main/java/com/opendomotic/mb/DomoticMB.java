/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.TesteService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jaques
 */
@Named
@SessionScoped
public class DomoticMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(DomoticMB.class.getName());

    @Inject
    private DeviceService deviceService;
    
    @Inject
    private TesteService testeService;
    
    private List<Device> listDeviceButton;
    
    public DomoticMB() {
        LOG.info("constructor");
    }
    
    @PostConstruct()
    public void init() {
        LOG.info("postConstruct");
        try {
            createListDeviceButton();            
            atualizar();            
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
    }
    
    private void createListDeviceButton() {
        listDeviceButton = new ArrayList<>();
        /*listDeviceButton.add(deviceService.getDevice(DeviceFactory.DEVICE_NAME_LUZ_SALA));
        listDeviceButton.add(deviceService.getDevice(DeviceFactory.DEVICE_NAME_LUZ_ESCRITORIO));
        listDeviceButton.add(deviceService.getDevice(DeviceFactory.DEVICE_NAME_VENTILADOR));
        listDeviceButton.add(deviceService.getDevice(DeviceFactory.DEVICE_NAME_CAFETEIRA));
        listDeviceButton.add(deviceService.getDevice(DeviceFactory.DEVICE_NAME_TESTE_ETHERNET));*/
    }
    
    public void atualizar() {
        LOG.info("atualizar");
        deviceService.updateDevices();
    }
    
    public void toogleDevice(Device device) {
        device.setValue(device.getValue() == 0 ? 1 : 0);
    }
    
    public void toggleAll() {
        boolean stateAll = !getStateAny();
        
        for (Device device : listDeviceButton) {
            device.setValue(stateAll ? 1 : 0);
        }
    }
    
    private boolean getStateAny() {
        for (Device device : listDeviceButton) {
            if (device.getValue() == 1)
                return true;
        }
        return false;
    }
    
    public String getCaptionAll() {
        return getStateAny() ? "Desligar tudo" : "Ligar tudo";
    }

    public List<DeviceProxy> getListDevice() {        
        return deviceService.getListDevice();
    }

    public List<Device> getListDeviceButton() {
        return listDeviceButton;
    }

    public String getTeste() {
        return testeService.getList().toString();
    }
    
    public void adicionarTeste() {
        testeService.adicionar();
    }
    
    public void excluirTeste() {
        testeService.excluir();
    }
    
}

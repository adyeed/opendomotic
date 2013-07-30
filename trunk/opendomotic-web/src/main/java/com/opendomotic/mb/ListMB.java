/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.DeviceProxy;
import com.opendomotic.service.DeviceService;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jaques
 */
@Named
@RequestScoped
public class ListMB implements Serializable {

    @Inject
    private DeviceService deviceService;
    
    private List<DeviceProxy> listDevice;
    
    public void atualizar() {
        deviceService.updateDeviceValues("listmb");
    }

    public List<DeviceProxy> getListDevice() {    
        if (listDevice == null) {
            listDevice = deviceService.createListDevice();
        }
        return listDevice;
    }

}

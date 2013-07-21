/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.DeviceProxy;
import com.opendomotic.service.DeviceService;
import java.io.Serializable;
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
public class ListMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(ListMB.class.getName());

    @Inject
    private DeviceService deviceService;
    
    private List<DeviceProxy> listDevice;
    
    @PostConstruct()
    public void init() {
        try {         
            atualizar();            
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
    }
    
    public void atualizar() {
        deviceService.updateDevices();
    }

    public List<DeviceProxy> getListDevice() {    
        if (listDevice == null) {
            listDevice = deviceService.createListDevice();
        }
        return listDevice;
    }

}

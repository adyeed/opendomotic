/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.GraphicDevice;
import com.opendomotic.service.DeviceService;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Jaques
 */
@ManagedBean
public class AdminMB {
    
    @EJB
    private DeviceService deviceService;
    
    private String deviceName;

    public void add() {
        GraphicDevice g = new GraphicDevice();
        g.setDeviceName(deviceName);
        deviceService.addGraphicDevice(g);
    }
    
    public void clear() {
        deviceService.getListGraphicDevice().clear();
    }
    
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
}

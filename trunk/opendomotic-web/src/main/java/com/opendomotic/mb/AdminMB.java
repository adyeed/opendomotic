/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.rest.GraphicDevice;
import com.opendomotic.service.DeviceService;
import java.util.List;
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
    private String imagem;

    public void add() {
        List<GraphicDevice> list = deviceService.getListGraphicDevice();
        
        int id = 1;
        if (!list.isEmpty())
            id = list.get(list.size()-1).getId()+1;        
        
        deviceService.getListGraphicDevice().add(
                new GraphicDevice(id, 0, 0, deviceName, imagem));
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
    
}

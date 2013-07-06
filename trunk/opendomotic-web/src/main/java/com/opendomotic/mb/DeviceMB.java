/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.service.DeviceConfigService;
import com.opendomotic.service.DeviceService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

/**
 *
 * @author jaques
 */
@ManagedBean
public class DeviceMB {
    
    private static final Logger LOG = Logger.getLogger(DeviceMB.class.getName());

    private DeviceConfig config = new DeviceConfig();
    private List<SelectItem> listProtocol;
    
    @EJB
    private DeviceService deviceService;
    
    @EJB
    private DeviceConfigService deviceConfigService;
    
    public void add() {
        LOG.info(config.toString());
        deviceConfigService.persist(config);
        deviceService.addDevice(config.createDevice());
    }
        
    public void remove(DeviceConfig config) {
        LOG.info(config.toString());
        deviceConfigService.remove(config);
        deviceService.removeDevice(config.getName());
    }

    public DeviceConfig getConfig() {
        return config;
    }

    public List<DeviceConfig> getListConfig() {
        return deviceConfigService.getListAll();
    }

    public List<SelectItem> getListProtocol() {
        if (listProtocol == null) {  
            listProtocol = new ArrayList<>();
            listProtocol.add(new SelectItem("RS485", "RS485"));
            listProtocol.add(new SelectItem("HTTP", "HTTP"));
        }
        return listProtocol;
    }
    
}

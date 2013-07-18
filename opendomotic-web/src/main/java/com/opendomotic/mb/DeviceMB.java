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
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@RequestScoped
public class DeviceMB {
    
    private static final Logger LOG = Logger.getLogger(DeviceMB.class.getName());

    @Inject 
    private DeviceService deviceService;
    
    @Inject 
    private DeviceConfigService deviceConfigService;
    
    private DeviceConfig config = new DeviceConfig();
    private List<SelectItem> listProtocol;
    private boolean dialogVisible = false;
    
    public void create() {
        edit(new DeviceConfig());
    }    
    
    public void save() {
        LOG.info(config.toString());
        deviceConfigService.save(config);
        deviceService.loadDevices();
        dialogVisible = false;
    }
        
    public void delete(DeviceConfig config) {
        LOG.info(config.toString());
        deviceConfigService.delete(config);
        deviceService.loadDevices();
    }
    
    public void edit(DeviceConfig config) {
        this.config = config;
        this.dialogVisible = true;
    }

    public DeviceConfig getConfig() {
        return config;
    }

    public List<DeviceConfig> getListConfig() {
        return deviceConfigService.findAll();
    }

    public List<SelectItem> getListProtocol() {
        if (listProtocol == null) {  
            listProtocol = new ArrayList<>();
            listProtocol.add(new SelectItem("RS485", "RS485"));
            listProtocol.add(new SelectItem("HTTP", "HTTP"));
        }
        return listProtocol;
    }

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }
    
}

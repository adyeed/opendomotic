/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DeviceProtocol;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.service.DeviceService;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@RequestScoped
public class ConfigMB extends AbstractCRUD<DeviceConfig> {
    
    @Inject 
    private DeviceService deviceService;
    
    @Inject 
    private DeviceConfigDAO dao;
    
    private List<DeviceProtocol> listProtocol;

    @Override
    public AbstractDAO<DeviceConfig> getDAO() {
        return dao;
    }
    
    @Override
    public void save() {
        super.save();
        deviceService.loadDevices();
    }
        
    @Override
    public void delete(DeviceConfig config) {
        super.delete(config);
        deviceService.loadDevices();
    }
    
    public List<DeviceProtocol> getListProtocol() {
        if (listProtocol == null) {
            listProtocol = Arrays.asList(DeviceProtocol.values());
        }
        return listProtocol;
    }
    
}

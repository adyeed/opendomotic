/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.api.Device;
import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DeviceProtocol;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.DeviceImageDAO;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    
    @Inject 
    private DeviceImageDAO deviceImageDAO;
    
    private List<DeviceProtocol> listProtocol;
    private Integer idImageDefault;
    private Integer idImageToggle;
    
    @PostConstruct
    public void init() {
        setOrderBy(new String[] {"name"});
    }
    
    @Override
    public AbstractDAO<DeviceConfig> getDAO() {
        return dao;
    }
    
    @Override
    public void save() {
        if (idImageDefault != null) {
            entity.setDeviceImageDefault(deviceImageDAO.findById(idImageDefault));
        }
        if (idImageToggle != null) {
            entity.setDeviceImageToggle(deviceImageDAO.findById(idImageToggle));
        }        
        super.save();
        deviceService.loadDevices();
    }
        
    @Override
    public void delete(DeviceConfig config) {
        super.delete(config);
        deviceService.loadDevices();
    }
    
    public void test(DeviceConfig config) {
        System.out.println("----- test begin");
        FacesMessage msg;
        try {
            Device device = config.createDevice();            
            msg = new FacesMessage("Success", "Value="+device.getValue().toString());
        } catch (Exception ex) {
            msg = new FacesMessage("Error", ex.getLocalizedMessage() + "|\n" + ex.getMessage() + "|\n" + ex.toString());  
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        System.out.println("----- test end");
    }
    
    public List<DeviceProtocol> getListProtocol() {
        if (listProtocol == null) {
            listProtocol = Arrays.asList(DeviceProtocol.values());
        }
        return listProtocol;
    }

    public Integer getIdImageDefault() {
        if (entity.getDeviceImageDefault() != null)
            return entity.getDeviceImageDefault().getId();
        return idImageDefault;
    }

    public void setIdImageDefault(Integer idImageDefault) {
        this.idImageDefault = idImageDefault;
    }

    public Integer getIdImageToggle() {
        if (entity.getDeviceImageToggle() != null)
            return entity.getDeviceImageToggle().getId();
        return idImageToggle;
    }

    public void setIdImageToggle(Integer idImageToggle) {
        this.idImageToggle = idImageToggle;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.api.Device;
import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DeviceProperty;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.DeviceImageDAO;
import com.opendomotic.service.dao.DevicePropertyDAO;
import java.util.ArrayList;
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
    
    @Inject
    private DevicePropertyDAO devicePropertyDAO;
    
    private Integer idImageDefault;
    private Integer idImageToggle;
    private String name1;
    private String name2;
    private String name3;
    private String value1;
    private String value2;
    private String value3;
    
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
        System.out.println("save--------");
        System.out.println(name1+"="+value1);
        System.out.println(name2+"="+value2);
        System.out.println(name3+"="+value3);
        
        devicePropertyDAO.deleteByConfig(entity);
        entity.setListDeviceProperty(new ArrayList<DeviceProperty>());
        if (!name1.isEmpty()) {
            entity.getListDeviceProperty().add(new DeviceProperty(entity, name1, value1));
        }
        if (!name2.isEmpty()) {
            entity.getListDeviceProperty().add(new DeviceProperty(entity, name2, value2));
        }        
        if (!name3.isEmpty()) {
            entity.getListDeviceProperty().add(new DeviceProperty(entity, name3, value3));
        } 
        
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

    @Override
    public void edit(DeviceConfig entity) {
        super.edit(entity);
        
        List<DeviceProperty> list = entity.getListDeviceProperty();
        if (list != null) {
            if (list.size() > 0) {
                name1 = list.get(0).getName();
                value1 = list.get(0).getValue();
            }
            if (list.size() > 1) {
                name2 = list.get(1).getName();
                value2 = list.get(1).getValue();
            }
            if (list.size() > 2) {
                name3 = list.get(2).getName();
                value3 = list.get(2).getValue();
            }
        }
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

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }
    
}

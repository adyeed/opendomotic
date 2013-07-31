/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import com.opendomotic.api.Device;
import com.opendomotic.factory.DeviceFactory;
import com.opendomotic.factory.DevicePropertyNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author jaques
 */
@Entity
public class DeviceConfig extends AbstractEntityName {
    
    @ManyToOne
    private DeviceImage deviceImageDefault;
    
    @ManyToOne
    private DeviceImage deviceImageToggle;

    @OneToMany(mappedBy = "deviceConfig", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DeviceProperty> listDeviceProperty;
    
    private String deviceClassName;

    public Device createDevice() throws ClassNotFoundException, InstantiationException, IllegalAccessException, DevicePropertyNotFoundException, IllegalArgumentException, InvocationTargetException {
        return DeviceFactory.createDevice(deviceClassName, createDeviceProperties());
    }
    
    private Map<String,Object> createDeviceProperties() {
        Map<String,Object> map = new LinkedHashMap<>();
        for (DeviceProperty property : listDeviceProperty) {
            map.put(property.getName(), property.getValue());
        }
        return map;
    }

    public DeviceImage getDeviceImageDefault() {
        return deviceImageDefault;
    }

    public void setDeviceImageDefault(DeviceImage deviceImageDefault) {
        this.deviceImageDefault = deviceImageDefault;
    }

    public DeviceImage getDeviceImageToggle() {
        return deviceImageToggle;
    }

    public void setDeviceImageToggle(DeviceImage deviceImageToggle) {
        this.deviceImageToggle = deviceImageToggle;
    }

    public List<DeviceProperty> getListDeviceProperty() {
        return listDeviceProperty;
    }

    public void setListDeviceProperty(List<DeviceProperty> listDeviceProperty) {
        this.listDeviceProperty = listDeviceProperty;
    }

    public String getDeviceClassName() {
        return deviceClassName;
    }

    public void setDeviceClassName(String deviceClassName) {
        this.deviceClassName = deviceClassName;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import com.opendomotic.device.Device;
import com.opendomotic.device.factory.DeviceFactory;
import com.opendomotic.device.factory.DevicePropertyNotFoundException;
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
    private DeviceImage deviceImageSwitch;

    @OneToMany(mappedBy = "deviceConfig", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DeviceProperty> listDeviceProperty;
    
    private String deviceClassName;
    private boolean enabled;
    private boolean switchable;
    private boolean history;
    private String format;
    private int threadId;

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

    public DeviceImage getDeviceImageSwitch() {
        return deviceImageSwitch;
    }

    public void setDeviceImageSwitch(DeviceImage deviceImageSwitch) {
        this.deviceImageSwitch = deviceImageSwitch;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSwitchable() {
        return switchable;
    }

    public void setSwitchable(boolean switchable) {
        this.switchable = switchable;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
    
}

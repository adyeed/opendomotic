/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model;

import com.opendomotic.api.Device;

/**
 *
 * @author jaques
 */
public class DeviceProxy implements Device {

    private Device device;
    private Object value;

    public DeviceProxy(Device device) {
        this.device = device;
    }
    
    @Override
    public String getName() {
        return device.getName();
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        device.setValue(value);
    }

    @Override
    public Object getValue() {
        return value;
    }
    
    public void updateValue() {
        value = device.getValue();
    }
    
}

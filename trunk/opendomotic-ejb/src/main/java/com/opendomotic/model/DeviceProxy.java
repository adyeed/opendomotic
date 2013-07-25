/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model;

import com.opendomotic.api.Device;
import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class DeviceProxy implements Device {

    private static final Logger LOG = Logger.getLogger(DeviceProxy.class.getName());

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
    
    public String getValueStr() {
        if (value != null)
            return value.toString();
        return "";
    }
    
    public boolean updateValue() {
        Object newValue = device.getValue();
        if (newValue == null) {
            LOG.severe("Error on reading " + getName());
            return false;
        }
        boolean changed = !value.equals(newValue);
        value = newValue;
        return changed;
    }
    
}

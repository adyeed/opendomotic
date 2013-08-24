/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model;

import com.opendomotic.device.Device;
import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class DeviceProxy implements Device {

    private static final Logger LOG = Logger.getLogger(DeviceProxy.class.getName());

    private final Device device;
    private Object value;

    public DeviceProxy(Device device) {
        this.device = device;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        try {
            device.setValue(value);
        } catch (Exception ex) {
            LOG.severe(ex.toString());
        }
    }

    @Override
    public Object getValue() {
        return value;
    }
    
    public boolean updateValue() {
        boolean changed = false;
        try {
            Object newValue = device.getValue();
            changed = !newValue.equals(value);
            value = newValue;
            
        } catch (Exception ex) {
            LOG.severe(ex.toString());
        }
        return changed;
    }

    @Override
    public String toString() {
        return device.toString();
    }
    
}

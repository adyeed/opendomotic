/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model;

import com.opendomotic.api.Device;
import java.util.logging.Level;
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
        //TO-DO: Se der erro, device deve retornar null e não -1
        if (newValue == -1) {
            LOG.severe("Error on reading " + getName());
            return false;
        }
        Object oldValue = value;
        value = newValue;
        return value != oldValue;
    }
    
}

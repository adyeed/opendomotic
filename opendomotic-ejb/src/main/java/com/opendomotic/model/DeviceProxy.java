/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model;

import com.opendomotic.device.Device;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class DeviceProxy implements Device {

    private static final Logger LOG = Logger.getLogger(DeviceProxy.class.getName());

    private final Device device;
    private Object value;
    private int millisResponse = -1;
    private int errors = 0;
    private DeviceHistory history; 

    public DeviceProxy(Device device, boolean saveHistory) {
        this.device = device;
        if (saveHistory) {
            history = new DeviceHistory();
        }
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        if (value != null) {
            try {
                device.setValue(value);
            } catch (Exception ex) {
                LOG.severe(ex.toString());
            }
        }
    }

    @Override
    public Object getValue() {
        return value;
    }

    public int getMillisResponse() {
        return millisResponse;
    }

    public int getErrors() {
        return errors;
    }
    
    public void incErrors() {
        errors++;
    }
    
    public boolean updateValue() throws Exception {
        long millis = System.currentTimeMillis();
        Object newValue = device.getValue();
        boolean changed = !newValue.equals(value);
        value = newValue;            
        millisResponse = (int) (System.currentTimeMillis() - millis);
        
        if (history != null) {
            history.add(new Date(), value);
        }
        
        return changed;
    }

    public DeviceHistory getHistory() {
        return history;
    }

    @Override
    public String toString() {
        return device.toString();
    }
    
}

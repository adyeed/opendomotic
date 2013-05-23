/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.serial;

import com.opendomotic.api.Device;

/**
 *
 * @author jaques
 */
public class SerialDevice implements Device {

    private String name;
    private int address;
    private int device;

    public SerialDevice(String name, int address, int device) {
        this.name = name;
        this.address = address;
        this.device = device;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setValue(Object value) {
        SerialBus.getInstance().writeDevice(address, device, (int) value);
    }

    @Override
    public Object getValue() {
        //return -1;
        return SerialBus.getInstance().readDevice(address, device);
    }
    
}

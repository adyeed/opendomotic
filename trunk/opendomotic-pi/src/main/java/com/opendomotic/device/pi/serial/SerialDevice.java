/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.pi.serial;

import com.opendomotic.device.Device;

/**
 *
 * @author jaques
 */
public class SerialDevice implements Device {

    private int address;
    private int device;

    @Override
    public void setValue(Object value) {
        SerialBus.getInstance().writeDevice(address, device, (int) value);
    }

    @Override
    public Object getValue() {
        int value = SerialBus.getInstance().readDevice(address, device);
        return value != -1 ? value : null;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "SerialDevice{" + "address=" + address + ", device=" + device + '}';
    }
    
}

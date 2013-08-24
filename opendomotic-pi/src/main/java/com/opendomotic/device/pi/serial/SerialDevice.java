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
public class SerialDevice implements Device<Integer> {

    private int address;
    private int device;

    @Override
    public void setValue(Integer value) {
        SerialBus.getInstance().writeDevice(address, device, value);
    }

    @Override
    public Integer getValue() throws Exception {
        int value = SerialBus.getInstance().readDevice(address, device);
        if (value == -1) {
            throw new Exception(String.format("Error on getValue of address=%d, device=%d", address, device));
        }
        return value;
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

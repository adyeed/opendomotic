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
public class SerialDevice<T> implements Device<T> {

    private int address;
    private int device;

    @Override
    public void setValue(T value) {
        SerialBus.getInstance().writeDevice(address, device, (Integer) value);
    }

    @Override
    public T getValue() throws Exception {
        return (T) getValueInt();
    }

    protected Integer getValueInt() throws Exception {
        Integer value = SerialBus.getInstance().readDevice(address, device);
        if (value == -1) {
            throw new Exception(String.format("Error on getValue of address=%d, device=%d", address, device));
        }
        return value;
    }
    
    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "SerialDevice{" + "address=" + address + ", device=" + device + '}';
    }
    
}

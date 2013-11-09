/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.device.pi;

import com.opendomotic.device.Device;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

/**
 *
 * @author Jaques Claudino
 */
public class I2CDevice implements Device<Integer> {
    
    private int address;
    private int device;
    
    private com.pi4j.io.i2c.I2CDevice i2c;

    @Override
    public Integer getValue() throws Exception {
        if (i2c == null) {
            i2c = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(address);
        }
        
        i2c.write((byte) device);
        Thread.sleep(5); //precisa de um intervalo

        byte[] buffer = new byte[2]; 
        i2c.read(buffer, 0, 2);

        int msb = buffer[0] & 0xff;
        int lsb = buffer[1] & 0xff;            
        int value = msb * 256 + lsb;
        
        return value;
    }

    @Override
    public void setValue(Integer value) throws Exception {
    }
    
    public void setAddress(int address) {
        this.address = address;
    }

    public void setDevice(int device) {
        this.device = device;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.device.pi.i2c;

import com.opendomotic.device.Device;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

/**
 *
 * @author Jaques Claudino
 * @param <T>
 */
public class I2CDevice<T> implements Device<T> {
    
    private int address;
    private int device;
    private int readBytes = 2;
    
    private com.pi4j.io.i2c.I2CDevice i2c;

    @Override
    public T getValue() throws Exception {
        return (T) getValueInt();
    }

    @Override
    public void setValue(T value) throws Exception {
        //TO-DO: Not implemented
    }
    
    protected Integer getValueInt() throws Exception {
        if (i2c == null) {
            i2c = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(address);
        }
        
        i2c.write((byte) device);
        Thread.sleep(5); //precisa de um intervalo
        
        byte[] buffer = new byte[readBytes]; 
        i2c.read(buffer, 0, readBytes);
        int value;
        
        if (readBytes == 4) {
            value = (buffer[0] << 24) + //o primeiro byte contém o sinal
                ((buffer[1] & 0xff) << 16) + //& 0xff pra ser unsigned. Outros bytes nao contém sinal
                ((buffer[2] & 0xff) << 8) +
                (buffer[3] & 0xff);
        } else { //2 bytes:
            int msb = buffer[0] & 0xff;
            int lsb = buffer[1] & 0xff;            
            value = msb * 256 + lsb;
        }
        
        return value;
    }
    
    public void setAddress(int address) {
        this.address = address;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public void setReadBytes(int readBytes) {
        this.readBytes = readBytes;
    }
    
}

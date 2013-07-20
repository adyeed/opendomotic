/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import com.opendomotic.api.Device;
import com.opendomotic.ethernet.HttpDevice;
import com.opendomotic.serial.SerialDevice;

/**
 *
 * @author jaques
 */
public enum DeviceProtocol {
    
    RS485 {
        @Override
        public Device createDevice(DeviceConfig config) {
            return new SerialDevice(
                    config.getName(), 
                    Integer.parseInt(config.getAddress()), 
                    Integer.parseInt(config.getParam()));
        }
    },
            
    HTTP {
        @Override
        public Device createDevice(DeviceConfig config) {
            return new HttpDevice(
                    config.getName(), 
                    config.getAddress(), 
                    config.getParam());
        }
    };
    
    public abstract Device createDevice(DeviceConfig config);
    
}

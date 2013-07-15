/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import com.opendomotic.api.Device;
import com.opendomotic.ethernet.HttpDevice;
import com.opendomotic.serial.SerialDevice;
import java.util.logging.Logger;
import javax.persistence.Entity;

/**
 *
 * @author jaques
 */
@Entity
public class DeviceConfig extends AbstractEntityName {
    
    private static final Logger LOG = Logger.getLogger(DeviceConfig.class.getName());
    
    private String address;
    private String param;
    private String protocol;

    //TO-DO: factory method para enum de protocol
    public Device createDevice() {
        try {
            switch (protocol) {
                case "RS485": return new SerialDevice(getName(), Integer.parseInt(address), Integer.parseInt(param));
                case "HTTP":  return new HttpDevice(getName(), address, param);
            }
        } catch (Exception e) {
            LOG.severe("Error on create device");
        }
        return null;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "DeviceConfig{" + "address=" + address + ", param=" + param + ", protocol=" + protocol + '}';
    }
    
}

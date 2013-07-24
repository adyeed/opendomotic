/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import com.opendomotic.api.Device;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author jaques
 */
@Entity
public class DeviceConfig extends AbstractEntityName {
    
    private static final Logger LOG = Logger.getLogger(DeviceConfig.class.getName());
    
    private String address;
    private String param;
    
    @Enumerated(EnumType.STRING)
    private DeviceProtocol protocol;

    //TO-DO: factory method para enum de protocol
    public Device createDevice() {
        try {
            if (protocol != null) {
                return protocol.createDevice(this);
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

    public DeviceProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(DeviceProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "DeviceConfig{" + "address=" + address + ", param=" + param + ", protocol=" + protocol + '}';
    }
    
}
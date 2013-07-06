/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import com.opendomotic.api.Device;
import com.opendomotic.ethernet.HttpDevice;
import com.opendomotic.serial.SerialDevice;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author jaques
 */
@Entity
public class DeviceConfig implements Serializable {
    
    @Id
    @GeneratedValue
    private Integer id;    
    private String name;
    private String address;
    private String param;
    private String protocol;

    //TO-DO: factory method para enum de protocol
    public Device createDevice() {
        switch (protocol) {
            case "RS485": return new SerialDevice(name, Integer.parseInt(address), Integer.parseInt(param));
            case "HTTP":  return new HttpDevice(name, address, param);
            default: return null;
        }
    }    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "DeviceConfig{" + "id=" + id + ", name=" + name + ", address=" + address + ", param=" + param + ", protocol=" + protocol + '}';
    }
    
}

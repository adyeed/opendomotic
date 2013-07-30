/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import com.opendomotic.api.Device;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 *
 * @author jaques
 */
@Entity
public class DeviceConfig extends AbstractEntityName {
    
    private static final Logger LOG = Logger.getLogger(DeviceConfig.class.getName());
    
    @ManyToOne
    private DeviceImage deviceImageDefault;
    
    @ManyToOne
    private DeviceImage deviceImageToggle;
    
    private String address;
    private String param;
    //private String className;
    
    @Enumerated(EnumType.STRING)
    private DeviceProtocol protocol;

    //TO-DO: factory method para enum de protocol
    public Device createDevice() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        /*if (className != null && !className.isEmpty()) {
            System.out.println("***** begin");
            Device device = (Device) Class.forName(className).newInstance();
            
            for (Method method : device.getClass().getDeclaredMethods()) {
                System.out.println(method);
                if (method.getName().equals("setName")) {
                    method.invoke(device, "Ldr escritório");
                } else if (method.getName().equals("setIp")) {
                    method.invoke(device, "192.168.10.47");
                } else if (method.getName().equals("setPath")) {
                    method.invoke(device, "?ldr");
                }                
            }
            
            return device;            
        }*/    
        if (protocol != null) {
            return protocol.createDevice(this);
        }
        return null;
    }

    public DeviceImage getDeviceImageDefault() {
        return deviceImageDefault;
    }

    public void setDeviceImageDefault(DeviceImage deviceImageDefault) {
        this.deviceImageDefault = deviceImageDefault;
    }

    public DeviceImage getDeviceImageToggle() {
        return deviceImageToggle;
    }

    public void setDeviceImageToggle(DeviceImage deviceImageToggle) {
        this.deviceImageToggle = deviceImageToggle;
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

    /*public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }*/

    @Override
    public String toString() {
        return "DeviceConfig{name=" + getName() + "address=" + address + ", param=" + param + ", protocol=" + protocol + '}';
    }
    
}

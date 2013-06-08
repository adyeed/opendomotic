/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jaques Claudino
 */
@XmlRootElement
public class DeviceValue {
    
    private String name;
    private String value;

    public DeviceValue() {
    }

    public DeviceValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}

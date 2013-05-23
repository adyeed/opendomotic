/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model;

import com.opendomotic.api.Device;
import java.util.Date;

/**
 *
 * @author jaques
 */
public class Clock implements Device {

    @Override
    public String getName() {
        return "Relógio";
    }

    @Override
    public void setValue(Object value) {
    }

    @Override
    public Object getValue() {
        return new Date();
    }
    
}

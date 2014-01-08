/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.device.pi.i2c;

/**
 *
 * @author Jaques Claudino
 */
public class DoubleI2CDevice extends I2CDevice<Double> {
 
    private double factor = 1;
    
    @Override
    public Double getValue() throws Exception {
        double value = super.getValueInt();
        return value / factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
    
}

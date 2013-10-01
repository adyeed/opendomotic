/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.pi.serial;

/**
 *
 * @author Jaques Claudino
 */
public class DoubleSerialDevice extends SerialDevice<Double> {

    private double fator = 10.0;
    
    @Override
    public Double getValue() throws Exception {
        double value = super.getValueInt();
        return value / fator;
    }

    public void setFator(double fator) {
        this.fator = fator;
    }
    
}
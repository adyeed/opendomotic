/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.pi;

import com.opendomotic.device.Device;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @author jaques
 */
public class DigitalPin implements Device {

    private Integer pin = 0;    
    private GpioPinDigitalOutput digitalPin;
        
    @Override
    public Object getValue() {   
        return getDigitalPin().isHigh() ? 1 : 0;
    }

    @Override
    public void setValue(Object value) {
        getDigitalPin().setState((Integer) value == 1);
    }    
    
    private GpioPinDigitalOutput getDigitalPin() {
        if (digitalPin == null) {
            digitalPin = createDigitalPin();
        }        
        return digitalPin;
    }
    
    private GpioPinDigitalOutput createDigitalPin() {
        Pin raspiPin;
        switch (pin) {
            case 1:  raspiPin = RaspiPin.GPIO_01; break;
            case 2:  raspiPin = RaspiPin.GPIO_02; break;
            case 3:  raspiPin = RaspiPin.GPIO_03; break;
            case 4:  raspiPin = RaspiPin.GPIO_04; break;
            case 5:  raspiPin = RaspiPin.GPIO_05; break;
            case 6:  raspiPin = RaspiPin.GPIO_06; break;
            case 7:  raspiPin = RaspiPin.GPIO_07; break;
            default: raspiPin = RaspiPin.GPIO_00; break;
        }
        return GpioFactory.getInstance().provisionDigitalOutputPin(raspiPin, PinState.LOW);
    }
    
    public void setPin(Integer pin) {
        if (pin != this.pin) {
            this.pin = pin;
            this.digitalPin = createDigitalPin();
        }
    }
    
}

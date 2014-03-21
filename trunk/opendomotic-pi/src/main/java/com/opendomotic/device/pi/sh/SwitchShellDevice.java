/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.device.pi.sh;

/**
 *
 * @author Jaques Claudino
 */
public class SwitchShellDevice extends ShellDevice<Integer> {

    @Override
    public Integer getValue() throws Exception {
        if (getReadCommand() != null) {
            return super.getValue();
        }
        return 0;
    }
    
    @Override
    public void setValue(Integer value) throws Exception {
        if (value.equals(1)) {
            super.setValue(value);
        }        
    }
    
}

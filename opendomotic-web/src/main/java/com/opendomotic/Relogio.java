/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic;

import com.opendomotic.api.Device;
import java.util.Date;

/**
 *
 * @author jaques
 */
public class Relogio implements Device {

    private int valor;

    @Override
    public Object getValue() {
        return new Date();
    }

    @Override
    public void setValue(Object value) {
    }
   
    public void setTeste(Integer valor) {
        this.valor = valor;
    }
    
}

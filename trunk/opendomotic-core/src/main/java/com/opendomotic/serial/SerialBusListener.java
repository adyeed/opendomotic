/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.serial;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;

/**
 *
 * @author Jaques
 */
public class SerialBusListener implements SerialDataListener {

    @Override
    public void dataReceived(SerialDataEvent event) {
        //byte ferra quando é maior que 127. Deve ser por causa do unsigned. Vem 1 byte a mais na frente com 194 ou 195
        System.out.print("listener RX=");     
        byte[] bufferRx = event.getData().getBytes();  
        for (int i=0; i<bufferRx.length; i++) {
            int rx = bufferRx[i] & 0xff; //unsigned
            System.out.print(rx+"|");
        }
        System.out.println();
    }
    
}

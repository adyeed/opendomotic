/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic;

import com.opendomotic.api.Device;
import com.opendomotic.ethernet.HttpDevice;
import java.util.List;

/**
 *
 * @author Jaques
 */
public class Main {

    public static final int ADDRESS_BROADCAST              = 255;
    public static final int ADDRESS_SALA                   = 1;
    public static final int ADDRESS_ESCRITORIO             = 2;
    
    public static final char DEVICE_ALL                    = 255;
    public static final char DEVICE_SALA_LUZ               = 1;
    public static final char DEVICE_SALA_POTENCIOMETRO     = 2;
    public static final char DEVICE_SALA_CAFETEIRA         = 3;
    
    public static final char DEVICE_ESCRITORIO_LUZ         = 1;
    public static final char DEVICE_ESCRITORIO_VENTILADOR  = 2;
    public static final char DEVICE_ESCRITORIO_LDR         = 3;
    public static final char DEVICE_ESCRITORIO_TEMP_LM35   = 4;
    public static final char DEVICE_ESCRITORIO_BRILHO      = 5;
    public static final char DEVICE_ESCRITORIO_UMIDADE     = 6;
    public static final char DEVICE_ESCRITORIO_TEMP_DHT11  = 7;
    
    private static List<Device> listDevice;

    private static Device luzSala;
    private static Device luzEscritorio;
    private static Device ventilador;
    private static Device cafeteira;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Device luz = new HttpDevice("Teste", "192.168.10.47", "?luzEscritorio");
        Device ven = new HttpDevice("Teste", "192.168.10.47", "?ventilador");
        luz.setValue(0);
        ven.setValue(1);
        Thread.sleep(2000);
        luz.setValue(1);
        ven.setValue(0);
        
        /*
        luzSala       = new SerialDevice("Luz sala",       ADDRESS_SALA, DEVICE_SALA_LUZ);
        luzEscritorio = new SerialDevice("Luz escritorio", ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_LUZ);
        ventilador    = new SerialDevice("Ventilador",     ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_VENTILADOR);
        cafeteira     = new SerialDevice("Cafeteira",      ADDRESS_SALA, DEVICE_SALA_CAFETEIRA);

        listDevice = new ArrayList<>();

        //SALA-----------------
        listDevice.add(luzSala);
        listDevice.add(cafeteira);
        listDevice.add(new SerialDevice("Potenciometro",  ADDRESS_SALA, DEVICE_SALA_POTENCIOMETRO));
        
        //ESCRITORIO-----------
        listDevice.add(luzEscritorio);
        listDevice.add(ventilador);
        listDevice.add(new SerialDevice("Brilho",           ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_BRILHO));
        listDevice.add(new SerialDevice("Ldr",              ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_LDR));
        listDevice.add(new SerialDevice("Temperatura LM35", ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_TEMP_LM35));
        listDevice.add(new SerialDevice("Temperatura DHT11",ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_TEMP_DHT11));
        listDevice.add(new SerialDevice("Umidade",          ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_UMIDADE));
        
        for (Device d : listDevice) {
            System.out.println(d.getName() + ": " + d.getValue());
        }
        */
        Thread.sleep(1000);
        System.exit(0);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model;

import com.opendomotic.api.Device;
import com.opendomotic.ethernet.HttpDevice;
import com.opendomotic.serial.SerialDevice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class DeviceFactory {
    
    private static final Logger LOG = Logger.getLogger(DeviceFactory.class.getName());
    
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
    
    public static final String DEVICE_NAME_LUZ_SALA            = "Luz sala";
    public static final String DEVICE_NAME_LUZ_ESCRITORIO      = "Luz escritorio";
    public static final String DEVICE_NAME_VENTILADOR          = "Ventilador";
    public static final String DEVICE_NAME_CAFETEIRA           = "Cafeteira";
    public static final String DEVICE_NAME_POTENCIOMETRO       = "Potenciometro";
    public static final String DEVICE_NAME_BRILHO              = "Brilho";
    public static final String DEVICE_NAME_LDR                 = "Ldr";
    public static final String DEVICE_NAME_TEMPERATURA_LM35    = "Temperatura LM35";
    public static final String DEVICE_NAME_TEMPERATURA_DHT11   = "Temperatura DHT11";
    public static final String DEVICE_NAME_UMIDADE             = "Umidade";
    public static final String DEVICE_NAME_RELOGIO             = "Relogio";
    public static final String DEVICE_NAME_TESTE_ETHERNET      = "Teste ethernet";
    
    private Map<String, DeviceProxy> mapDevice;
    private List<DeviceProxy> listDevice;
    
    public void createDevices() {
        LOG.info("Creating devices...");
        mapDevice = new HashMap<>();
        listDevice = new ArrayList<>();
                
        //SALA-----------------
        addSerialDevice(DEVICE_NAME_LUZ_SALA,      ADDRESS_SALA, DEVICE_SALA_LUZ);
        addSerialDevice(DEVICE_NAME_CAFETEIRA,     ADDRESS_SALA, DEVICE_SALA_CAFETEIRA);
        addSerialDevice(DEVICE_NAME_POTENCIOMETRO, ADDRESS_SALA, DEVICE_SALA_POTENCIOMETRO);
        
        //ESCRITORIO-----------
        addSerialDevice(DEVICE_NAME_LUZ_ESCRITORIO,    ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_LUZ);
        addSerialDevice(DEVICE_NAME_VENTILADOR,        ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_VENTILADOR);
        addSerialDevice(DEVICE_NAME_BRILHO,            ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_BRILHO);
        addSerialDevice(DEVICE_NAME_LDR,               ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_LDR);
        addSerialDevice(DEVICE_NAME_TEMPERATURA_LM35,  ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_TEMP_LM35);
        addSerialDevice(DEVICE_NAME_TEMPERATURA_DHT11, ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_TEMP_DHT11);
        addSerialDevice(DEVICE_NAME_UMIDADE,           ADDRESS_ESCRITORIO, DEVICE_ESCRITORIO_UMIDADE);
        
        //LOCAL----------------
        addDevice(DEVICE_NAME_RELOGIO, new Clock());
        addDevice(DEVICE_NAME_TESTE_ETHERNET, new HttpDevice(DEVICE_NAME_TESTE_ETHERNET, "192.168.10.47", "?luzEscritorio"));
    }
    
    private void addSerialDevice(String name, int address, int index) {
        addDevice(name, new SerialDevice(name, address, index));
    }
    
    private void addDevice(String name, Device device) {
        DeviceProxy serialDevice = new DeviceProxy(device);
        mapDevice.put(name, serialDevice);
        listDevice.add(serialDevice);
    }    

    public Map<String, DeviceProxy> getMapDevice() {
        return mapDevice;
    }

    public List<DeviceProxy> getListDevice() {
        return listDevice;
    }
    
}

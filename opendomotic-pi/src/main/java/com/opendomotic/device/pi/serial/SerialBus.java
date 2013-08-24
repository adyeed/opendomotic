/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.pi.serial;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaques
 */
public class SerialBus {
    
    private static final char COMMAND_READ  = 'r';
    private static final char COMMAND_WRITE = 'w';    
    
    private static final int INDEX_DATA_LENGTH = 1;
    private static final int INDEX_DATA_BEGIN  = 2;
    
    private static final int READ_ATTEMPTS = 3; //tentativas para ler o device
    private static final int TIMEOUT = 100;
    
    private static SerialBus instance;
    private static final Logger LOG = Logger.getLogger(SerialBus.class.getName());
    private static final boolean SHOW_LOG = false;
    
    //GPIO:
    private GpioController gpio;
    private GpioPinDigitalOutput pin1;
    private Serial serial = SerialFactory.createInstance();
    private SerialDataListener serialListener = new SerialBusListener();
    
    private SerialBus() {        
        gpio = GpioFactory.getInstance();
        pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "PIN_RE_485", PinState.LOW);
        serial.open(Serial.DEFAULT_COM_PORT, 115200);
        serial.addListener(serialListener);
    }
    
    public static SerialBus getInstance() {
        if (instance == null) {
            instance = new SerialBus();
        }
        return instance;
    }
    
    public void shutdown() {
        if (gpio != null) {
            serial.shutdown();
            serial = null;
            gpio.shutdown();
            gpio = null;
        }
    }
    
    public void serialWrite(byte[] bufferTx, boolean canWaitResponse) {
        try {
            pin1.high(); //tx mode
            serial.write(bufferTx);
            serial.flush();
            Thread.sleep(5); //esperar slave receber 5 era pouco para 9600
            pin1.low(); //rx mode
            
            if (canWaitResponse) {
                waitResponse();
            }
        } catch (IllegalStateException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);    
        }
    }
    
    public void serialWrite(byte[] bufferTx) {
        serialWrite(bufferTx, true);
    }
    
    public int serialRead(byte[] bufferTx, int attempts) {
        serial.removeListener(serialListener);
        try {
            serialWrite(bufferTx, false);
            int[] bufferRx = waitResponse();
            if (bufferRx != null) {
                return getBufferInt(bufferRx, 0); //conseguiu ler
            } else if (attempts > 0) {
                if (SHOW_LOG) {
                    LOG.warning("Error on reading device. Trying again...");
                }
                return serialRead(bufferTx, attempts-1);
            } else {
                LOG.severe("Failed on read device.");
                return -1;
            }
        } finally {
            serial.addListener(serialListener);
        }
    }
    
    private int[] waitResponse() {
        int[] bufferRx = new int[300];
        int indexRx=0;
        long millisStart = System.currentTimeMillis();        
        
        StringBuilder log = new StringBuilder("waitResponse RX=");
        while (System.currentTimeMillis()-millisStart < TIMEOUT) {
            if (serial.availableBytes() > 0) {
                bufferRx[indexRx++] = serial.read() & 0xff; //unsigned
                log.append(bufferRx[indexRx-1]);
                log.append("|");
            } else if (indexRx > 0) {
                break;
            }
        }
        
        long tempo = System.currentTimeMillis()-millisStart;
        if (SHOW_LOG && tempo > 30) {
            log.append(tempo);
            log.append(" ms");
            LOG.info(log.toString());
        }
        
        if (indexRx > 0 && isCheckSumOK(bufferRx))
            return Arrays.copyOf(bufferRx, indexRx);
        return null;        
    }
    
    private boolean isCheckSumOK(int[] bufferRx) {
        int dataLength = bufferRx[INDEX_DATA_LENGTH];
        int checksum = 0;
        int i;
        for (i=0; i<dataLength+INDEX_DATA_BEGIN; i++) {
            checksum += bufferRx[i];
        }

        boolean isOK = (bufferRx[i] == checksum / 256) && (bufferRx[i+1] == checksum % 256);

        if (SHOW_LOG && !isOK) {
            StringBuilder log = new StringBuilder("CHECKSUM ERROR: ");
            log.append(checksum);
            log.append(" rx=");
            log.append(bufferRx[i]);
            log.append("|");
            log.append(bufferRx[i+1]);
            log.append(" expected=");
            log.append(checksum / 256);
            log.append("|");
            log.append(checksum % 256);
            LOG.severe(log.toString());
        }

        return isOK;
    }
    
    private byte[] getBufferTx(int address, int command, int device, int value) {
        byte[] tx = new byte[8];
        byte len = 4;
        
        tx[0] = (byte)address;
        tx[1] = len;
        tx[2] = (byte)command;
        tx[3] = (byte)device;
        tx[4] = (byte)(value / 256);
        tx[5] = (byte)(value % 256);
        
        int checksum = address + len + command + device + value;
        tx[6] = (byte)(checksum / 256);
        tx[7] = (byte)(checksum % 256);
        
        return tx;
    }
    
    private int getBufferInt(int[] bufferRx, int dataIndex) {
        return bufferRx[INDEX_DATA_BEGIN + dataIndex] * 256 + 
               bufferRx[INDEX_DATA_BEGIN + dataIndex + 1];
    }
    
    public int readDevice(int address, int device) {
        return serialRead(getBufferTx(address, COMMAND_READ, device, 0), READ_ATTEMPTS);
    }
    
    public void writeDevice(int address, int device, int value) {
        serialWrite(getBufferTx(address, COMMAND_WRITE, device, value));
    }
    
}

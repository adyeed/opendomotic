/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.device.pi.sh;

import com.opendomotic.device.Device;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Jaques Claudino
 */
public class ShellDevice implements Device<String> {

    private String readCommand;
    private String writeCommand;
    
    @Override
    public String getValue() throws Exception {
        if (getReadCommand() != null) {
            return getRunTimeExecLine(getReadCommand());
        }        
        return null;
    }

    @Override
    public void setValue(String value) throws Exception {
        if (getWriteCommand() != null) {
            getRunTimeExecLine(getWriteCommand());
        }
    }
    
    /**
     * 
     * @param command
     * example1: sensors | grep 'CPU Temp:' | awk '{print $3}' | grep -o '[0-9.]\+'
     * example2: /opt/vc/bin/vcgencmd measure_temp| egrep "[0-9.]{4,}" -o
     * @return 
     * @throws java.io.IOException 
     */
    public String getRunTimeExecLine(String command) throws IOException {
        String line;
        Process p = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", command});
        try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            while ((line = in.readLine()) != null) {
                return line;
            }
        }
        return null;
    }

    public String getReadCommand() {
        return readCommand;
    }

    public void setReadCommand(String readCommand) {
        this.readCommand = readCommand;
    }

    public String getWriteCommand() {
        return writeCommand;
    }

    public void setWriteCommand(String writeCommand) {
        this.writeCommand = writeCommand;
    }
    
}

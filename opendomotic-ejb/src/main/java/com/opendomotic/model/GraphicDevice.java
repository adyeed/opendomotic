/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jaques
 */
@XmlRootElement
public class GraphicDevice {
   
    private String deviceName;
    private int x;
    private int y;
    private String src; //image.src

    public GraphicDevice() {
    }
    
    public GraphicDevice(String deviceName, int x, int y, String src) {
        this.deviceName = deviceName;
        this.x = x;
        this.y = y;
        this.src = src;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jaques
 */
@XmlRootElement
public class DevicePositionRest {
    
    private int id;
    private int x;
    private int y;
    private String name;
    private boolean switchable;
    private String imageDefault; 
    private String imageSwitch;

    public DevicePositionRest() {
    }

    public DevicePositionRest(int id, int x, int y, String name, boolean switchable, String imageDefault, String imageSwitch) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.switchable = switchable;
        this.imageDefault = imageDefault;
        this.imageSwitch = imageSwitch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
   
    public boolean isSwitchable() {
        return switchable;
    }

    public void setSwitchable(boolean switchable) {
        this.switchable = switchable;
    }
    
    public String getImageDefault() {
        return imageDefault;
    }

    public void setImageDefault(String imageDefault) {
        this.imageDefault = imageDefault;
    }

    public String getImageSwitch() {
        return imageSwitch;
    }

    public void setImageSwitch(String imageSwitch) {
        this.imageSwitch = imageSwitch;
    }
    
}

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
    private int type;
    private String imageDefault; 
    private String imageSwitch;

    public DevicePositionRest() {
    }

    public DevicePositionRest(int id, int x, int y, String name, int type, String imageDefault, String imageSwitch) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

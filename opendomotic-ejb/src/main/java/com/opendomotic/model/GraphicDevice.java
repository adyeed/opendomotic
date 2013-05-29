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
   
    private int id;
    private int x;
    private int y;
    private String name; //image.alt
    private String src; //image.src

    public GraphicDevice() {
    }
    
    public GraphicDevice(int id, int x, int y, String name, String src) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.src = src;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "GraphicDevice{" + "id=" + id + ", name=" + name + ", x=" + x + ", y=" + y + ", src=" + src + '}';
    }
    
}

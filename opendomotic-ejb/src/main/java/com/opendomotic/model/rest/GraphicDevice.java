/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.rest;

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
    private String src0; //image.src
    private String src1; 

    public GraphicDevice() {
    }
    
    public GraphicDevice(int id, int x, int y, String name, String src0, String src1) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.src0 = src0;
        this.src1 = src1;
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

    public String getSrc0() {
        return src0;
    }

    public void setSrc0(String src0) {
        this.src0 = src0;
    }

    public String getSrc1() {
        return src1;
    }

    public void setSrc1(String src1) {
        this.src1 = src1;
    }

    @Override
    public String toString() {
        return "GraphicDevice{" + "id=" + id + ", x=" + x + ", y=" + y + ", name=" + name + ", src0=" + src0 + ", src1=" + src1 + '}';
    }
    
}

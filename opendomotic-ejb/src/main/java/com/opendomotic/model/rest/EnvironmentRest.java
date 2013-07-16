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
public class EnvironmentRest {
    
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}

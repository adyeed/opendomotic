/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.api;

/**
 *
 * @author jaques
 */
public interface Device {

    String getName();
    Object getValue();
    void setValue(Object value);
    
}

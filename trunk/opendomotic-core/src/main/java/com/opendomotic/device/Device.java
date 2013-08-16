/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device;

/**
 *
 * @author jaques
 * @param <T>
 */
public interface Device<T> {

    T getValue();
    void setValue(T value);
    
}

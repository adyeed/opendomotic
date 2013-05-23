/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.api;

import java.io.Serializable;

/**
 *
 * @author jaques
 */
public interface Device extends Serializable {

    String getName();
    Object getValue();
    void setValue(Object value);
    
}

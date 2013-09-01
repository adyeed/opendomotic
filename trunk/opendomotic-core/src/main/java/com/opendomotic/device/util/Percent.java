/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.util;

/**
 *
 * @author Jaques Claudino
 */
public class Percent {
 
    public static int getPercent(int value, int min, int max, boolean checkRange) {
        int range = max - min;
        if (range == 0)
            return 0;
        
        int percent = (value - min) * 100 / range;
        if (checkRange) {
            return Math.max(0, Math.min(100, percent));
        } else {
            return percent;
        }
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.websocket;

import javax.ejb.Singleton;

/**
 *
 * @author jaques
 */
@Singleton
public class WebSocketService {
        
    private static final String UPDATE_DEVICE_VALUES = "updateDeviceValues";
    
    private BroadcastMessenger broadcastMessenger;
    
    public void send(String msg) {
        if (broadcastMessenger != null) {
            broadcastMessenger.sendBroadcast(msg);
        }
    }
    
    public void sendUpdateDeviceValues(String origin) {
        send(UPDATE_DEVICE_VALUES + ": " + origin);
    }
    
    public void setBroadcastMessenger(BroadcastMessenger broadcastMessenger) {
        this.broadcastMessenger = broadcastMessenger;
    }
    
}

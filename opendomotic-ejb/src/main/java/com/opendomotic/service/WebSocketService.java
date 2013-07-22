/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import javax.ejb.Singleton;

/**
 *
 * @author jaques
 */
@Singleton
public class WebSocketService {
        
    private static final String UPDATE_DEVICE_VALUES = "updateDeviceValues";
    
    private BroadcastWebsocket broadcastWebsocket;
    
    public void send(String msg) {
        if (broadcastWebsocket != null) {
            broadcastWebsocket.sendBroadcast(msg);
        }
    }
    
    public void sendUpdateDeviceValues(String origin) {
        send(UPDATE_DEVICE_VALUES + ": " + origin);
    }
    
    public void setBroadcastWebsocket(BroadcastWebsocket broadcastWebsocket) {
        this.broadcastWebsocket = broadcastWebsocket;
    }
    
}

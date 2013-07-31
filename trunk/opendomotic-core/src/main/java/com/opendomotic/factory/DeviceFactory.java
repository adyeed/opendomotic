/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.factory;

import com.opendomotic.api.Device;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaques
 */
public class DeviceFactory {
    
    private static final Logger LOG = Logger.getLogger(DeviceFactory.class.getName());
    
    public static Device createDevice(String className, Map<String, Object> properties) throws ClassNotFoundException, InstantiationException, IllegalAccessException, DevicePropertyNotFoundException, IllegalArgumentException, InvocationTargetException {
        Class clazz = Class.forName(className);
        Device device = (Device) clazz.newInstance();
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            Method method = getMethod(device, property.getKey());   
            if (method == null || method.getParameterTypes().length != 1) {
                LOG.log(Level.SEVERE, "property {0} not found", property.getKey());
                throw new DevicePropertyNotFoundException();
            }
            
            if (isStringToInteger(property.getValue(), method)) {
                method.invoke(device, Integer.parseInt((String) property.getValue()));
            } else {
                method.invoke(device, property.getValue());
            }
        }        
        return device;
    }
    
    private static Method getMethod(Device device, String name) { //TO-DO: get from superclasses
        for (Method method : device.getClass().getDeclaredMethods()) {
            if (method.getName().equalsIgnoreCase("set"+name)){
                return method;
            }
        }
        return null;
    }    
    
    private static boolean isStringToInteger(Object value, Method method) {
        String p = method.getParameterTypes()[0].getName();
        return value instanceof String && (p.equals(int.class.getName()) || p.equals(Integer.class.getName()));
    }
    
}

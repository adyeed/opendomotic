package com.opendomotic.service;

import com.opendomotic.service.websocket.WebSocketService;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.device.Device;
import com.opendomotic.model.DeviceHistory;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DeviceConfig;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author jaques
 */
@Singleton
@Startup
@Lock(LockType.WRITE)
@AccessTimeout(value = 10, unit = TimeUnit.SECONDS)
public class DeviceService {

    private static final Logger LOG = Logger.getLogger(DeviceService.class.getName());
    
    private Map<String, DeviceProxy> mapDevice;
    private boolean scheduleInitialized = false;

    @Inject
    private WebSocketService webSocketService;
    
    @Inject
    private DeviceConfigDAO configDAO;
       
    @PostConstruct
    public void init() {
        LOG.info("DeviceService init...");
        loadDevices();
    }
           
    public void loadDevices() {
        LOG.info("Loading devices...");

        mapDevice = Collections.synchronizedMap(new LinkedHashMap<String, DeviceProxy>());
        for (DeviceConfig config : configDAO.findAllEnabled()) {
            try {
                LOG.log(Level.INFO, "creating {0}", config.getName());
                Device device = config.createDevice();
                if (device != null) {
                    mapDevice.put(config.getName(), new DeviceProxy(device, config.isHistory()));
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error creating device: {0}", ex.toString());
            }
        }
    }
    
    @Schedule(second = "*/30", minute = "*", hour = "*")
    public void updateDeviceValuesTimer() {
        updateDeviceValues();
        
        if (!scheduleInitialized) { //to avoid ConcurrentAccessTimeoutException from JobService 
            scheduleInitialized = true;
            LOG.info("Schedule initialized.");
        }
    }
    
    @Asynchronous
    public void updateDeviceValuesAsync() {
        updateDeviceValues();
    }
    
    private void updateDeviceValues() {
        long millisTotal = System.currentTimeMillis();
        
        for (Entry<String, DeviceProxy> entry : mapDevice.entrySet()) {
            String deviceName = entry.getKey();
            DeviceProxy device = entry.getValue();
            boolean sendWebSocket = false;
                   
            try {
                if (device.updateValue()) {
                    sendWebSocket = true;
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error updating device: {0} | {1}", new Object[] {deviceName, ex.toString()});
                device.setValue(null);
                sendWebSocket = true;
            }
            logTime(device.toString(), device.getMillisResponse(), 1000);
            
            if (sendWebSocket) {
                //TO-DO: apenas enviar para os clientes que estao observando o ambiente
                webSocketService.sendUpdateDeviceValue(deviceName, getDeviceValueAsString(deviceName));
            }
        }
        
        logTime("Total", System.currentTimeMillis()-millisTotal, 2000);
    }
           
    @Lock(LockType.READ)
    public Object getDeviceValue(String deviceName) {
        DeviceProxy device = mapDevice.get(deviceName);
        if (device != null) {
            return device.getValue();
        } else {
            return null;
        }
    }
    
    @Lock(LockType.READ)
    public DeviceHistory getDeviceHistory(DeviceConfig config) {
        DeviceProxy device = mapDevice.get(config.getName());
        if (device != null) {
            return device.getHistory();
        } else {
            return null;
        }
    }
    
    @Lock(LockType.READ)
    public String getDeviceValueAsString(DeviceConfig config) {
        Object value = getDeviceValue(config.getName());
        String valueStr = "";
        try {
            if (value != null && config.getFormat() != null && !config.getFormat().isEmpty()) {
                valueStr = String.format(config.getFormat(), value);
            } else {
                valueStr = String.valueOf(value);
            }    
        } catch (Exception ex) {
            LOG.severe(ex.toString());
        }
        return valueStr;
    }
    
    @Lock(LockType.READ)
    public String getDeviceValueAsString(String deviceName) {
        DeviceConfig config = configDAO.findByName(deviceName);
        return getDeviceValueAsString(config);
    }
    
    @Lock(LockType.READ)
    public int getDeviceMillisResponse(DeviceConfig config) {
        DeviceProxy device = mapDevice.get(config.getName());
        if (device != null) {
            return device.getMillisResponse();
        } else {
            return -1;
        }
    }

    public void setDeviceValue(String deviceName, Object value) {
        DeviceProxy device = mapDevice.get(deviceName);
        if (device != null) {
            device.setValue(value);
            webSocketService.sendUpdateDeviceValue(deviceName, getDeviceValueAsString(deviceName)); 
        }
    }
    
    public void switchDeviceValue(String deviceName) {
        Object value = getDeviceValue(deviceName);        
        Object newValue;
        if (value instanceof String) {
            newValue = value.equals("1") ? "0" : "1";
        } else {
            newValue = value.equals(1) ? 0 : 1;
        }            
        setDeviceValue(deviceName, newValue);
    }
    
    @Lock(LockType.READ)
    public boolean isScheduleInitialized() {
        return scheduleInitialized;
    }
    
    private void logTime(String item, long elapsedMillis, int limit) {
        if (elapsedMillis > limit) {
            LOG.log(Level.WARNING, "Slow reading device: {0} ms | {1}", new Object[] {elapsedMillis, item});
        }
    }
    
}

package com.opendomotic.service;

import com.opendomotic.service.websocket.WebSocketService;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.device.Device;
import com.opendomotic.model.DeviceHistory;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DeviceConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
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
    private Map<Integer, List<DeviceProxy>> mapWorker; //key=threadId
    private boolean scheduleInitialized = false;
    private int millisResponse = -1;
    private int millisWebSocket = -1;

    @Inject
    private DeviceWorker deviceWorker;
    
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
        mapWorker = Collections.synchronizedMap(new LinkedHashMap<Integer, List<DeviceProxy>>());
        for (DeviceConfig config : configDAO.findAllEnabled()) {
            try {
                LOG.log(Level.INFO, "creating {0}", config.getName());
                Device device = config.createDevice();
                if (device != null) {
                    DeviceProxy deviceProxy = new DeviceProxy(device, config.getName(), config.isHistory());
                    mapDevice.put(config.getName(), deviceProxy);
                    
                    //worker to updateDevices asynchronously:
                    List<DeviceProxy> listDevice = mapWorker.get(config.getThreadId());
                    if (listDevice == null) {
                        listDevice = new ArrayList<>();
                        mapWorker.put(config.getThreadId(), listDevice);
                    }
                    listDevice.add(deviceProxy);                    
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
        
        try {
            //trigger threads:
            long millisStart = System.currentTimeMillis();
            List<Future<List<DeviceProxy>>> listFuture = new ArrayList<>();
            for (Entry<Integer, List<DeviceProxy>> entry : mapWorker.entrySet()) {
                listFuture.add(deviceWorker.updateDevices(entry.getKey(), entry.getValue()));        
            }
            
            //join threads:
            List<List<DeviceProxy>> listUpdated = new ArrayList<>();
            for (Future<List<DeviceProxy>> future : listFuture) {
                listUpdated.add(future.get());
            }
            millisResponse = (int) (System.currentTimeMillis()-millisStart);
            
            //send websocket:           
            millisStart = System.currentTimeMillis();                        
            for (List<DeviceProxy> list : listUpdated) {
                for (DeviceProxy device : list) {
                    //TO-DO: apenas enviar websocket para os clientes que estao observando o ambiente
                    webSocketService.sendUpdateDeviceValue(device.getName(), getDeviceValueAsString(device.getName()));
                }
            }
            millisWebSocket = (int) (System.currentTimeMillis()-millisStart);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error updating devices: {0}", ex.toString());
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
    public int getDeviceMillisResponse(DeviceConfig config) {
        DeviceProxy device = mapDevice.get(config.getName());
        if (device != null) {
            return device.getMillisResponse();
        } else {
            return -1;
        }
    }
    
    @Lock(LockType.READ)
    public int getDeviceMillisResponseSum() {
        return millisResponse;
    }
    
    @Lock(LockType.READ)
    public String getDeviceMillisResponseFmt() {
        StringBuilder sb = new StringBuilder();       
        
        for (Entry<Integer, List<DeviceProxy>> entry : mapWorker.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            int millis = 0;
            for (DeviceProxy device : entry.getValue()) {
                if (device.getMillisResponse() > 0) {
                    millis += device.getMillisResponse();
                }
            }
            sb.append(millis);
            sb.append(" | ");
        }        
        
        sb.append("total=");
        sb.append(millisResponse);
        sb.append(" | websocket=");
        sb.append(millisWebSocket);
        
        return sb.toString();
    }
    
    @Lock(LockType.READ)
    public int getDeviceErrors(DeviceConfig config) {
        DeviceProxy device = mapDevice.get(config.getName());
        if (device != null) {
            return device.getErrors();
        } else {
            return -1;
        }
    }
    
    @Lock(LockType.READ)
    public int getDeviceErrorsSum() {
        int sum = 0;
        for (DeviceProxy device : mapDevice.values()) {
            if (device.getErrors() > 0) {
                sum += device.getErrors();
            }
        }        
        return sum;
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
    
}

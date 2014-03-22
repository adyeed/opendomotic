package com.opendomotic.mb.crud;

import com.opendomotic.device.Device;
import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DeviceProperty;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.DevicePropertyDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class ConfigMB extends AbstractCRUD<DeviceConfig> {
    
    private static final Logger LOG = Logger.getLogger(ConfigMB.class.getName());
        
    @Inject 
    private DeviceService deviceService;
    
    @Inject 
    private DeviceConfigDAO deviceConfigDAO;
    
    @Inject
    private DevicePropertyDAO devicePropertyDAO;
    
    private List<DeviceProperty> listDeviceProperty;

    @PostConstruct
    public void init() {
        setOrderBy(new String[] {"threadId", "name"});
    }
    
    @Override
    public AbstractDAO<DeviceConfig> getDAO() {
        return deviceConfigDAO;
    }

    @Override
    public void create() {
        super.create(); 
        entity.setEnabled(true);
    }
    
    @Override
    public void save() {     
        super.save();
        updateDeviceProperty();
        deviceService.loadDevices();
        deviceService.updateDeviceValuesAsync();
    }
        
    private void updateDeviceProperty() {
        int i = 0;
        while (i < listDeviceProperty.size()) {
            DeviceProperty p = listDeviceProperty.get(i);            
            if (p.getName().isEmpty()) {
                if (p.getId() != null) {
                    devicePropertyDAO.delete(p);
                }
                listDeviceProperty.remove(i);
            } else {
                p.setDeviceConfig(entity);
                devicePropertyDAO.save(p);
                i++;
            }
        }
        entity.setListDeviceProperty(listDeviceProperty);
    }
    
    @Override
    public void delete(DeviceConfig config) {
        super.delete(config);
        deviceService.loadDevices();
    }

    @Override
    public void edit(DeviceConfig entity) {
        super.edit(entity);
        
        listDeviceProperty = new ArrayList<>();
        if (entity.getListDeviceProperty() != null) {
            for (DeviceProperty p : entity.getListDeviceProperty()) {
                listDeviceProperty.add(p);
            }
        }        
        listDeviceProperty.add(new DeviceProperty());
        listDeviceProperty.add(new DeviceProperty());
    }
    
    public String getDeviceValueAsString(DeviceConfig config) {
        return deviceService.getDeviceValueAsString(config);
    }
    
    public int getDeviceMillisResponse(DeviceConfig config) {
        return deviceService.getDeviceMillisResponse(config);
    }
    
    public int getDeviceMillisResponseSum() {
        return deviceService.getDeviceMillisResponseSum();
    }
    
    public String getDeviceMillisResponseFmt() {
        return deviceService.getDeviceMillisResponseFmt();
    }
    
    public int getDeviceErrors(DeviceConfig config) {
        return deviceService.getDeviceErrors(config);
    }
    
    public int getDeviceErrorsSum() {
        return deviceService.getDeviceErrorsSum();
    }
    
    public void test(DeviceConfig config) {
        FacesMessage msg;
        try {
            Device device = config.createDevice();            
            msg = new FacesMessage("Success", "Value="+device.getValue().toString());
        } catch (Exception ex) {
            LOG.severe(ex.toString());
            msg = new FacesMessage("Error", ex.toString());  
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }    

    public List<DeviceProperty> getListDeviceProperty() {
        return listDeviceProperty;
    }
    
}

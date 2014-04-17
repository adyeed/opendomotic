package com.opendomotic.mb.crud;

import com.opendomotic.device.Device;
import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DeviceProperty;
import com.opendomotic.model.entity.DeviceType;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.CriteriaGetter;
import com.opendomotic.service.dao.DevicePropertyDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class ConfigMB extends AbstractSelectableCRUD<DeviceConfig> {
    
    private static final Logger LOG = Logger.getLogger(ConfigMB.class.getName());
        
    @Inject 
    private DeviceService deviceService;
    
    @Inject 
    private DeviceConfigDAO deviceConfigDAO;
    
    @Inject
    private DevicePropertyDAO devicePropertyDAO;
    
    private List<DeviceProperty> listDeviceProperty;
    private List<DeviceConfig> listAllOrderByName;

    @Override
    protected CriteriaGetter.OrderGetter getOrderGetter() {
        return new CriteriaGetter.OrderGetter() {
            @Override
            public List getListOrder(CriteriaQuery query, CriteriaBuilder builder, Root root) {
                return Arrays.asList(
                        builder.asc(root.get("threadId")), 
                        builder.asc(root.get("name")));
            }
        };
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
        listAllOrderByName = null;
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
        listAllOrderByName = null;
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
    
    public List<DeviceConfig> getListAllOrderByName() {
        if (listAllOrderByName == null) {
            listAllOrderByName = deviceConfigDAO.findAllOrderByName();
        }
        return listAllOrderByName;
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
    
    public DeviceType[] getDeviceTypes() {
        return DeviceType.values();
    }
    
}

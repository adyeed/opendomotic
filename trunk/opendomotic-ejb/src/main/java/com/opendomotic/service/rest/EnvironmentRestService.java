/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.rest;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DeviceImage;
import com.opendomotic.model.entity.DevicePosition;
import com.opendomotic.model.entity.Environment;
import com.opendomotic.model.rest.DevicePositionRest;
import com.opendomotic.model.rest.EnvironmentRest;
import com.opendomotic.service.dao.EnvironmentDAO;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jaques
 */
@Path("/environment")
public class EnvironmentRestService {
    
    private static final String IMAGE_PATH = "../resources/images/"; //TO-DO: como resolver caminho?
    
    @Inject
    private EnvironmentDAO environmentDAO;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public EnvironmentRest getEnvironmentRest(@QueryParam("id") int idEnvironment) {
        Environment environment = environmentDAO.findById(idEnvironment);
        if (environment != null) {
            EnvironmentRest e = new EnvironmentRest();            
            List<DevicePositionRest> list = new ArrayList<>();
            
            for (DevicePosition position : environment.getListDevicePosition()) {  
                DeviceConfig config = position.getDeviceConfig();
                if (config.isEnabled()) {
                    list.add(new DevicePositionRest(
                       position.getId(), 
                       position.getX(),
                       position.getY(),
                       position.getDeviceConfig().getName(), 
                       position.getDeviceConfig().isSwitchable(),
                       getDeviceImageFileName(position.getDeviceConfig().getDeviceImageDefault()),
                       getDeviceImageFileName(position.getDeviceConfig().getDeviceImageSwitch())));
                }
            }            
            
            e.setFileName(IMAGE_PATH + environment.getFileName()); 
            e.setListDevicePositionRest(list);
            return e;
        }
        return null;
    }
    
    private String getDeviceImageFileName(DeviceImage deviceImage) {
        if (deviceImage == null)
            return null;
        return IMAGE_PATH + deviceImage.getFileName();
    }
    
}

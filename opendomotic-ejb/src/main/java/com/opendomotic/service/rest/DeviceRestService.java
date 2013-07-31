/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.rest;

import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DevicePosition;
import com.opendomotic.model.rest.DeviceValueRest;
import com.opendomotic.service.dao.DevicePositionDAO;
import com.opendomotic.service.DeviceService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jaques
 */
@Path("/device")
public class DeviceRestService {
    
    private static final Logger LOG = Logger.getLogger(DeviceRestService.class.getName());

    @Inject
    private DeviceService deviceService;

    @Inject
    private DevicePositionDAO positionDAO;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/value")
    public List<DeviceValueRest> getListValue() {
        List<DeviceValueRest> list = new ArrayList<>();
        for (Entry<String, DeviceProxy> entry : deviceService.getMapDevice().entrySet()) {
            String deviceName = entry.getKey();
            DeviceProxy device = entry.getValue();
            list.add(new DeviceValueRest(deviceName, device.getValueStr()));
        }
        return list;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/move")
    public String move(
            @QueryParam("id") int id,
            @QueryParam("x") int x,
            @QueryParam("y") int y) {
        
        LOG.info(String.format("id=%d x=%d y=%d", id, x, y));
        
        DevicePosition position = positionDAO.findById(id);
        if (position != null) {
            position.setX(x);
            position.setY(y);
            positionDAO.save(position);
            return "OK";
        }
        return "Device não encontrado";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/toggle")
    public String toggle(@QueryParam("name") String name) {
        LOG.log(Level.INFO, "name={0}", name);
        Device device = deviceService.getDevice(name);
        if (device != null) {
            deviceService.toggleDeviceValue(device);
            deviceService.updateDeviceValuesAsync();   
            return device.getValue().toString();
        } else {
            return "Device não encontrado";
        }
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.rest;

import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.rest.DeviceValue;
import com.opendomotic.model.rest.GraphicDevice;
import com.opendomotic.service.DeviceService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
public class DeviceRest {
    
    private static final Logger LOG = Logger.getLogger(DeviceRest.class.getName());

    @EJB
    private DeviceService deviceService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GraphicDevice> getListGraphicDevice() {
        return deviceService.getListGraphicDevice();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/value")
    public List<DeviceValue> getListValue() {
        List<DeviceValue> list = new ArrayList<>();
        for (DeviceProxy device : deviceService.getListDevice()) {
            String value = "";
            try {
                device.updateValue();
                value = device.getValue().toString();
            } catch (Exception e) {
                LOG.warning(e.toString());
            }            
            list.add(new DeviceValue(device.getName(), value));
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
        
        for (GraphicDevice device : deviceService.getListGraphicDevice()) {
            if (device.getId() == id) {
                device.setX(x);
                device.setY(y);
                LOG.info(device.toString());
                return "OK";
            }
        }
        return "Device não encontrado";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/toggle")
    public String toggle(@QueryParam("name") String name) {
        Device device = deviceService.getDevice(name);
        if (device != null) {
            device.setValue(device.getValue() == 1 ? 0 : 1);
            return "OK";
        } else {
            return "Device não encontrado";
        }
    }
    
}

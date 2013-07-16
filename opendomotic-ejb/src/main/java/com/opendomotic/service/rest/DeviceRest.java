/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.rest;

import com.opendomotic.api.Device;
import com.opendomotic.model.DeviceProxy;
import com.opendomotic.model.entity.DevicePosition;
import com.opendomotic.model.entity.Environment;
import com.opendomotic.model.rest.DeviceValue;
import com.opendomotic.model.rest.GraphicDevice;
import com.opendomotic.service.DevicePositionService;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.EnvironmentService;
import java.util.ArrayList;
import java.util.List;
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
public class DeviceRest {
    
    private static final Logger LOG = Logger.getLogger(DeviceRest.class.getName());

    @Inject
    private DeviceService deviceService;

    @Inject
    private DevicePositionService positionService;
    
    @Inject
    private EnvironmentService environmentService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GraphicDevice> getListGraphicDevice() {
        List<GraphicDevice> list = new ArrayList<>();
        for (DevicePosition position : positionService.findAll()) { //TO-DO: adicionar filtro idEnvironment           
            list.add(new GraphicDevice(
                    position.getId(), 
                    position.getX(),
                    position.getY(),
                    position.getDeviceConfig().getName(), 
                    "../resources/images/" + position.getDeviceImage().getFileName(), //TO-DO: lista de imagens
                    null));            
        }
        return list;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/list")
    public List<GraphicDevice> getListGraphicDevice(@QueryParam("idEnvironment") int idEnvironment) {
        List<GraphicDevice> list = new ArrayList<>();
        Environment environment = environmentService.findById(idEnvironment);
        if (environment != null) {
            for (DevicePosition position : environment.getListDevicePosition()) {  
               list.add(new GraphicDevice(
                   position.getId(), 
                   position.getX(),
                   position.getY(),
                   position.getDeviceConfig().getName(), 
                   "../resources/images/" + position.getDeviceImage().getFileName(), //TO-DO: lista de imagens
                   null));            
            }
        }
        return list;
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
        
        LOG.info(String.format("id=%d x=%d y=%d", id, x, y));
        
        DevicePosition position = positionService.findById(id);
        if (position != null) {
            position.setX(x);
            position.setY(y);
            positionService.save(position);
            return "OK";
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.rest;

import com.opendomotic.model.GraphicDevice;
import com.opendomotic.service.DeviceService;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jaques
 */
@Path("/device")
public class DeviceRest {
    
    @EJB
    private DeviceService deviceService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GraphicDevice> getListGraphicDevice() {
        return deviceService.getListGraphicDevice();
    }
    
}

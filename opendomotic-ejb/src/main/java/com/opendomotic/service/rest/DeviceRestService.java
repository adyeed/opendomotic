package com.opendomotic.service.rest;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DevicePosition;
import com.opendomotic.model.rest.DeviceValueRest;
import com.opendomotic.service.dao.DevicePositionDAO;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.DeviceConfigDAO;
import java.util.ArrayList;
import java.util.List;
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
    private DeviceConfigDAO configDAO;
    
    @Inject
    private DevicePositionDAO positionDAO;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/value")
    public List<DeviceValueRest> list() {
        List<DeviceValueRest> list = new ArrayList<>();
        for (DeviceConfig config : configDAO.findAllEnabled()) {
            list.add(new DeviceValueRest(
                    config.getName(), 
                    deviceService.getDeviceValueAsString(config)));
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
        return "Device not found";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/switch")
    public String switchDevice(@QueryParam("name") String name) {
        LOG.log(Level.INFO, "name={0}", name);

        deviceService.switchDeviceValue(name);
        deviceService.updateDeviceValuesAsync();   
        return "OK";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/set")
    public String set(
            @QueryParam("name") String name, 
            @QueryParam("value") int value) {
        LOG.log(Level.INFO, "name={0} enabled={1}", new Object[] {name, value});
        
        for (DeviceConfig config : configDAO.findAllByNameLike(name)) {
            LOG.info(config.getName());
            deviceService.setDeviceValue(config.getName(), value);
        }
       
        deviceService.updateDeviceValuesAsync();   
        return "OK";
    }
    
}

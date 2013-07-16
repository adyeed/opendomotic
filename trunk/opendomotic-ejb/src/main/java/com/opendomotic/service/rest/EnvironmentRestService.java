/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.rest;

import com.opendomotic.model.entity.Environment;
import com.opendomotic.model.rest.EnvironmentRest;
import com.opendomotic.service.EnvironmentService;
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
    
    @Inject
    private EnvironmentService environmentService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public EnvironmentRest getListGraphicDevice(@QueryParam("id") int idEnvironment) {
        Environment environment = environmentService.findById(idEnvironment);
        if (environment != null) {
            EnvironmentRest e = new EnvironmentRest();
            e.setFileName("../resources/images/" + environment.getFileName()); //TO-DO: como resolver caminho?
            return e;
        }
        return null;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.entity.Environment;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.EnvironmentDAO;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class HomeMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(HomeMB.class.getName());
    
    @Inject
    private EnvironmentDAO environmentDAO;
    
    @Inject
    private DeviceService deviceService;
    
    private int idEnvironment;
    
    @PostConstruct
    public void init() {
        Environment environment = environmentDAO.findFirst();
        if (environment != null) {
            idEnvironment = environment.getId();
        }
        LOG.log(Level.INFO, "HomeMB init id={0}", idEnvironment);
    }
    
    public int getIdEnvironment() {
        return idEnvironment;
    }

    public void setIdEnvironment(int idEnvironment) {
        this.idEnvironment = idEnvironment;
    }
    
}

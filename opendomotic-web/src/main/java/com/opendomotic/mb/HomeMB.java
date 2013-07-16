/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.service.EnvironmentService;
import java.io.Serializable;
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
    private EnvironmentService environmentService;
    
    private Integer idEnvironment;
    
    @PostConstruct
    public void init() {
        idEnvironment = environmentService.findFirst().getId();
        LOG.info("HomeMB init id="+idEnvironment);
    }
    
    public Integer getIdEnvironment() {
        return idEnvironment;
    }

    public void setIdEnvironment(Integer idEnvironment) {
        this.idEnvironment = idEnvironment;
    }
    
}

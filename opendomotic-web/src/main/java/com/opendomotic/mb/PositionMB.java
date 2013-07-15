/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.entity.Environment;
import com.opendomotic.service.EnvironmentService;
import com.opendomotic.service.DevicePositionService;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class PositionMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(PositionMB.class.getName());
    
    @Inject 
    private EnvironmentService environmentService;
    
    @Inject 
    private DevicePositionService positionService;
    
    private Environment environment; //To-DO: eliminar e usar só o id
    private Integer idEnvironment;    
    private Integer idConfig;    
    private Integer idImage;
    
    @PostConstruct
    public void init() {
        environment = environmentService.findFirst();
        idEnvironment = environment.getId();
    }
    
    public void valueChangeMethod(ValueChangeEvent e) {
        idEnvironment = (Integer) e.getNewValue();
        LOG.info("xxx idEnvironment="+idEnvironment);
        refresh();
    }
    
    public void refresh() {
        environment = environmentService.findById(idEnvironment);
        System.out.println("teste");
        System.out.println("size="+environment.getListDevicePosition().size());
    }
    
    public void add() {
        positionService.save(idEnvironment, idConfig, idImage);       
    }
    
    public void clear() {
        positionService.deleteByIdEnvironment(idEnvironment);
    }
    
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Integer getIdEnvironment() {
        return idEnvironment;
    }

    public void setIdEnvironment(Integer idEnvironment) {
        this.idEnvironment = idEnvironment;
    }

    public Integer getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(Integer idConfig) {
        this.idConfig = idConfig;
    }
    
    public Integer getIdImage() {
        return idImage;
    }

    public void setIdImage(Integer idImage) {
        this.idImage = idImage;
    }
    
}

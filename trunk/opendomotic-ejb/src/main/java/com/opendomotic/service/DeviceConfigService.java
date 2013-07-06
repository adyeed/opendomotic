/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.model.entity.DeviceConfig;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jaques
 */
@Stateless
public class DeviceConfigService {
    
    @PersistenceContext
    private EntityManager em;
    
    public void persist(DeviceConfig deviceConfig) {
        em.persist(deviceConfig);
    }
    
    public void remove(DeviceConfig deviceConfig) {
        em.remove(deviceConfig);
    }
    
    public List<DeviceConfig> getListAll() {
        return em.createQuery("SELECT d from DeviceConfig d ORDER BY d.name").getResultList();
    }
    
}

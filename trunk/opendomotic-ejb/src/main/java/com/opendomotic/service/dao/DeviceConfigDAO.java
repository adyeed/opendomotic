/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.dao;

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
public class DeviceConfigDAO extends AbstractDAO<DeviceConfig> {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Class getEntityClass() {
        return DeviceConfig.class;
    }
    
    public List<DeviceConfig> findByName(String name) {
        return em
                .createQuery("select c from DeviceConfig c where c.name like ?1")
                .setParameter(1, "%"+name+"%")
                .getResultList();
    }
    
    public List<DeviceConfig> findAllEnabled() {
        return em
                .createQuery("select c from DeviceConfig c where c.enabled = true")
                .getResultList();
    }
    
}

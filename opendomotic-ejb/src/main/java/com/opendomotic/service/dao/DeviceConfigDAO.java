/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.dao;

import com.opendomotic.model.entity.DeviceConfig;
import javax.ejb.Stateless;

/**
 *
 * @author jaques
 */
@Stateless
public class DeviceConfigDAO extends AbstractDAO<DeviceConfig> {

    @Override
    public Class getEntityClass() {
        return DeviceConfig.class;
    }    
    
}

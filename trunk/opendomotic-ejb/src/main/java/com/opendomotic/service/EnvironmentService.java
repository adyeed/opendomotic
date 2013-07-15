/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.model.entity.Environment;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jaques
 */
@Stateless
public class EnvironmentService extends AbstractSessionFacade<Environment> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Class getEntityClass() {
        return Environment.class;
    }
    
}

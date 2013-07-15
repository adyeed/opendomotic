/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author jaques
 */
public abstract class AbstractSessionFacade<T> {
    
    private static final Logger LOG = Logger.getLogger(AbstractSessionFacade.class.getName());
    
    public abstract EntityManager getEntityManager();
    
    public abstract Class<T> getEntityClass(); 

    protected CriteriaQuery<T> createCriteriaQuery() {
        EntityManager em = getEntityManager();
        Class<T> entityClass = getEntityClass();
        
        CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return cq;
    }
    
    public List<T> findAll() {
        return getEntityManager()
                .createQuery(createCriteriaQuery())
                .getResultList();
    }
    
    public T findById(Integer id) {
        return getEntityManager().find(getEntityClass(), id);
    }
    
    public T findFirst() {
        try {
            return getEntityManager()
                    .createQuery(createCriteriaQuery())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOG.warning(e.toString());
            return null;
        }            
    }

    public T save(T entity) {
        return getEntityManager().merge(entity);
    }

    public void delete(T entity) {
        EntityManager em = getEntityManager();
        em.remove(em.merge(entity));
    }

    public T createEntity() throws InstantiationException, IllegalAccessException {
        return getEntityClass().newInstance();
    }
    
}

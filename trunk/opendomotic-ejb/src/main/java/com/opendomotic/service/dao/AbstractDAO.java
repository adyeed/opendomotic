/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.dao;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author jaques
 */
public abstract class AbstractDAO<T> {
    
    private static final Logger LOG = Logger.getLogger(AbstractDAO.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    public abstract Class<T> getEntityClass(); 

    protected CriteriaQuery<T> createCriteriaQuery() {
        Class<T> entityClass = getEntityClass();        
        CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return cq;
    }
    
    public List<T> findAll() {
        return em
                .createQuery(createCriteriaQuery())
                .getResultList();
    }
    
    public T findById(Integer id) {
        return em.find(getEntityClass(), id);
    }
    
    public T findFirst() {
        try {
            return em
                    .createQuery(createCriteriaQuery())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOG.warning(e.toString());
            return null;
        }            
    }

    public T save(T entity) {
        return em.merge(entity);
    }

    public void delete(T entity) {
        em.remove(em.merge(entity));
    }

    public T createEntity() {
        try {
            return getEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.severe(ex.toString());
            return null;
        }
    }
    
}

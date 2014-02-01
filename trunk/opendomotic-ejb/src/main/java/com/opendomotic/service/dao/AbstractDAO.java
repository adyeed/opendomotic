/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 *
 * @author jaques
 */
public abstract class AbstractDAO<T> {

    private static final Logger LOG = Logger.getLogger(AbstractDAO.class.getName());

    @PersistenceContext
    private EntityManager em;

    public Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public EntityManager getEntityManager() {
        return em;
    }

    protected CriteriaQuery<T> createCriteriaQuery(String[] orderBy) {
        Class<T> entityClass = getEntityClass();
        CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);

        if (orderBy != null) {
            List<Order> listOrder = new ArrayList<>();
            for (String fieldName : orderBy) {
                listOrder.add(em.getCriteriaBuilder().asc(from.get(fieldName)));
            }
            cq.orderBy(listOrder);
        }

        return cq;
    }

    public List<T> findAll(String[] orderBy) {
        return em
                .createQuery(createCriteriaQuery(orderBy))
                .getResultList();
    }

    public List<T> findAll() {
        return findAll(null);
    }

    public T findById(Integer id) {
        return em.find(getEntityClass(), id);
    }

    public T findFirst() {
        try {
            return em
                    .createQuery(createCriteriaQuery(null))
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.AbstractEntityId;
import com.opendomotic.service.dao.AbstractDAO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jaques
 */
public abstract class AbstractCRUD<T extends AbstractEntityId> implements Serializable {
    
    protected T entity;
    private List<T> listAll;
    private String[] orderBy;
    protected boolean visible = false;
 
    public abstract AbstractDAO<T> getDAO();
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public T getEntity() {
        if (entity == null) {
            entity = getDAO().createEntity();
        }
        return entity;
    }
    
    public void create() {
        edit(getDAO().createEntity());
    }
    
    public void save() {
        getDAO().save(entity);
        listAll = null;
        visible = false;
    }
    
    public void cancel() {
        visible = false;
    }
    
    public void delete(T entity) {
        getDAO().delete(entity);
        listAll = null;
    }
    
    public void edit(T entity) {
        this.entity = entity;
        this.visible = true;
    }
    
    public List<T> getListAll() {
        if (listAll == null) {
            listAll = getDAO().findAll(orderBy);
        }
        return listAll;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }
    
}

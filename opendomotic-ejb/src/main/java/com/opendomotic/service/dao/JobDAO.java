/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.dao;

import com.opendomotic.model.entity.Job;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author jaques
 */
@Stateless
public class JobDAO extends AbstractDAO<Job> {
    
    public List<Job> findAllEnabled() {
        return getEntityManager()
                .createQuery("select j from Job j where j.enabled = true order by j.index")
                .getResultList();
    }
    
}

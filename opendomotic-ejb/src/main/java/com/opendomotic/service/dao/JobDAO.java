/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service.dao;

import com.opendomotic.model.entity.Job;
import javax.ejb.Stateless;

/**
 *
 * @author jaques
 */
@Stateless
public class JobDAO extends AbstractDAO<Job> {

    @Override
    public Class<Job> getEntityClass() {
        return Job.class;
    }
    
}

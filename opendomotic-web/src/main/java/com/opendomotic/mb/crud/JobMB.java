/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.Job;
import com.opendomotic.model.entity.JobOperator;
import com.opendomotic.service.dao.AbstractDAO;
import com.opendomotic.service.dao.DeviceConfigDAO;
import com.opendomotic.service.dao.JobDAO;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@RequestScoped
public class JobMB extends AbstractCRUD<Job> {
    
    @Inject
    private JobDAO jobDAO;
    
    @Inject
    private DeviceConfigDAO deviceConfigDAO;
    
    private Integer idInput;
    private Integer idOutput;
    private List<JobOperator> listOperator;
    private int tabActiveIndex;

    @Override
    public AbstractDAO<Job> getDAO() {
        return jobDAO;
    }

    @Override
    public void save() {
        if (tabActiveIndex == 0) {
            entity.setInputDate(null);
        } else { //inputDate:
            entity.setInput(null);
            entity.setOperator(null);
            entity.setExpectValue(null);
        }
        
        if (idInput != null) {
            entity.setInput(deviceConfigDAO.findById(idInput));
        }        
        if (idOutput != null) {
            entity.setOutput(deviceConfigDAO.findById(idOutput));
        }
        super.save();
    }

    @Override
    public void edit(Job entity) {
        idInput = getIdDeviceConfig(entity.getInput());
        idOutput = getIdDeviceConfig(entity.getOutput());
        tabActiveIndex = idInput != null || entity.getId() == null ? 0 : 1;
        super.edit(entity); 
    }
    
    private Integer getIdDeviceConfig(DeviceConfig deviceConfig) {
        return deviceConfig != null ? deviceConfig.getId() : null;         
    }
    
    public Integer getIdInput() {
        return idInput;
    }

    public void setIdInput(Integer idInput) {
        this.idInput = idInput;
    }

    public Integer getIdOutput() {
        return idOutput;
    }

    public void setIdOutput(Integer idOutput) {
        this.idOutput = idOutput;
    }
    
    public List<JobOperator> getListOperator() {
        if (listOperator == null) {
            listOperator = Arrays.asList(JobOperator.values());
        }
        return listOperator;
    }

    public int getTabActiveIndex() {
        return tabActiveIndex;
    }

    public void setTabActiveIndex(int tabActiveIndex) {
        this.tabActiveIndex = tabActiveIndex;
    }
    
}

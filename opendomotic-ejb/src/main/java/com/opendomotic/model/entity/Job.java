/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jaques
 * sintax: input operator expectValue: output = actionValue
 * example: ldr <= 200: luz = 1 
 */
@Entity
public class Job extends AbstractEntityId {
    
    @ManyToOne
    private DeviceConfig input;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date inputDate;
    
    private JobOperator operator;
    private Serializable expectValue;
    private Serializable actionValue;
    
    @ManyToOne
    private DeviceConfig output;
    
    private boolean deleteAfterExecute;

    public String getInputDescription() {
        if (input != null) {
            return input.getName() + " " + operator.toString() + " " + expectValue;
        }
        if (inputDate != null) {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(inputDate);
        }
        return null;
    }
    
    public String getOutputDescription() {
        if (output != null) {
            return output.getName() + " = " + actionValue;
        }
        return null;
    }
    
    public DeviceConfig getInput() {
        return input;
    }

    public void setInput(DeviceConfig input) {
        this.input = input;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public JobOperator getOperator() {
        return operator;
    }

    public void setOperator(JobOperator operator) {
        this.operator = operator;
    }

    public Serializable getExpectValue() {
        return expectValue;
    }
    
    public Integer getExpectValueAsInt() {
        if (expectValue instanceof String)
            return Integer.parseInt((String) expectValue);
        return (Integer) expectValue;
    }

    public void setExpectValue(Serializable expectValue) {
        this.expectValue = expectValue;
    }

    public Serializable getActionValue() {
        return actionValue;
    }
    
    public Integer getActionValueAsInt() {
        if (actionValue instanceof String)
            return Integer.parseInt((String) actionValue);
        return (Integer) actionValue;
    }

    public void setActionValue(Serializable actionValue) {
        this.actionValue = actionValue;
    }

    public DeviceConfig getOutput() {
        return output;
    }

    public void setOutput(DeviceConfig output) {
        this.output = output;
    }

    public boolean isDeleteAfterExecute() {
        return deleteAfterExecute;
    }

    public void setDeleteAfterExecute(boolean deleteAfterExecute) {
        this.deleteAfterExecute = deleteAfterExecute;
    }

    @Override
    public String toString() {
        return getInputDescription() + " | " + getOutputDescription();
    }

}

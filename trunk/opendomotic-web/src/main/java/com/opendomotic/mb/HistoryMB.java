/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.mb;

import com.opendomotic.model.DeviceHistory;
import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.service.DeviceService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author Jaques Claudino
 */
@Named
@SessionScoped
public class HistoryMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(HistoryMB.class.getName());
    
    @Inject 
    private DeviceService deviceService;
    
    private DeviceConfig config;
    private DeviceHistory history;
    private CartesianChartModel linearModel;   
    private LineChartSeries seriesEA;
    private DeviceHistory.DeviceHistoryItem min;
    private DeviceHistory.DeviceHistoryItem max;
    
    public String history(DeviceConfig config) {     
        this.config = config;
        this.history = deviceService.getDeviceHistory(config);
        if (history != null) {
            createLinearModel();
        }        
        return "/user/history";
    }
       
    private void createLinearModel() {  
        linearModel = new CartesianChartModel();  
        
        seriesEA = new LineChartSeries();
        seriesEA.setLabel(config.getName());
        linearModel.addSeries(seriesEA);  
        min = null;
        max = null;
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT-2"));        

        for (DeviceHistory.DeviceHistoryItem item : history.getList()) {
            String dt = sdf.format(item.getDate());
            Integer value = item.getValueAsInt();
            
            if (min == null || value < min.getValueAsInt()) {
                min = item;
            }
            if (max == null || value > max.getValueAsInt()) {
                max = item;
            }
            
            seriesEA.set(dt, value);
        }
        
    }
    
    public void itemSelect(ItemSelectEvent event) {      
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Selecionado:", "index "+ event.getSeriesIndex()));  
    }
    
    public CartesianChartModel getLinearModel() {  
        return linearModel;
    }

    public DeviceHistory getHistory() {
        return history;
    }

    public DeviceHistory.DeviceHistoryItem getMin() {
        return min;
    }

    public DeviceHistory.DeviceHistoryItem getMax() {
        return max;
    }
    
}

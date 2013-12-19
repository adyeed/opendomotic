/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.opendomotic.mb;

import com.opendomotic.model.DeviceHistory;
import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.service.DeviceService;
import com.opendomotic.service.dao.DeviceConfigDAO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
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
    private static final int DEFAULT_INTERVAL = 10;
    
    @Inject 
    private DeviceService deviceService;
    
    @Inject 
    private DeviceConfigDAO deviceConfigDAO;
    
    private DeviceConfig config;
    private DeviceHistory history;
    private CartesianChartModel linearModel;   
    private LineChartSeries lineSeries;
    private DeviceHistory.DeviceHistoryItem min;
    private DeviceHistory.DeviceHistoryItem max;
    private DeviceHistory.DeviceHistoryItem last;
    private int minutesInterval = DEFAULT_INTERVAL;
    private int idConfig;
    private List<DeviceConfig> listDeviceConfigHistory;
    
    @PostConstruct
    public void init() {
        listDeviceConfigHistory = deviceConfigDAO.findAllWithHistory();
        if (!listDeviceConfigHistory.isEmpty()) {
            refresh(listDeviceConfigHistory.get(0));
        }
    }
       
    //uses idConfig setted by selectOneMenu:
    public void refresh() {
        config = deviceConfigDAO.findById(idConfig);
        if (config != null) {
            refresh(config);
        }        
    }
    
    public void refresh(DeviceConfig config) {
        this.idConfig = config.getId();
        this.config = config;
        this.history = deviceService.getDeviceHistory(config);
        if (history != null) {
            createLinearModel();
        }
    }
    
    private void createLinearModel() {
        lineSeries = new LineChartSeries();
        lineSeries.setLabel(config.getName());
        linearModel = new CartesianChartModel();
        linearModel.addSeries(lineSeries);  
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); 
        
        min = null;
        max = null;
        last = null;   

        Iterator<DeviceHistory.DeviceHistoryItem> it = history.getList().iterator();
        while (it.hasNext()) {
            DeviceHistory.DeviceHistoryItem item = it.next();
            Integer value = item.getValueAsInt();
            
            if (min == null || value < min.getValueAsInt()) {
                min = item;
            }
            if (max == null || value > max.getValueAsInt()) {
                max = item;
            }
                        
            if (last == null || !it.hasNext() || item.getDate().getTime() - last.getDate().getTime() >= minutesInterval*60*1000) {
                String dt = sdf.format(item.getDate());
                lineSeries.set(dt, value);
                last = item;
            }
        }
        
    }
    
    public void configChangeMethod(ValueChangeEvent e) {
        idConfig = (Integer) e.getNewValue();
        refresh();
    }
    
    public void intervalChangeMethod(ValueChangeEvent e) {
        minutesInterval = (Integer) e.getNewValue();
        refresh();
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

    public DeviceHistory.DeviceHistoryItem getLast() {
        return last;
    }

    public int getMinutesInterval() {
        return minutesInterval;
    }

    public void setMinutesInterval(int minutesInterval) {
        this.minutesInterval = minutesInterval;
    }

    public int getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(int idConfig) {
        this.idConfig = idConfig;
    }

    public List<DeviceConfig> getListDeviceConfigHistory() {
        return listDeviceConfigHistory;
    }
    
}

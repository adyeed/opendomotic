/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.rest.GraphicDevice;
import com.opendomotic.service.DeviceService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

/**
 *
 * @author Jaques
 */
@ManagedBean
public class AdminMB {
    
    @EJB
    private DeviceService deviceService;
    
    private String deviceName;
    private String imagem0;
    private String imagem1;
    
    private List<SelectItem> listImage;

    public void add() {
        List<GraphicDevice> list = deviceService.getListGraphicDevice();
        
        int id = 1;
        if (!list.isEmpty())
            id = list.get(list.size()-1).getId()+1;        
        
        deviceService.getListGraphicDevice().add(
                new GraphicDevice(id, 0, 0, deviceName, imagem0, imagem1.isEmpty() ? null : imagem1));
    }
    
    public void clear() {
        deviceService.getListGraphicDevice().clear();
    }
    
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getImagem0() {
        return imagem0;
    }

    public void setImagem0(String imagem0) {
        this.imagem0 = imagem0;
    }

    public String getImagem1() {
        return imagem1;
    }

    public void setImagem1(String imagem1) {
        this.imagem1 = imagem1;
    }
    
    public List<SelectItem> getListImage() {
        if (listImage == null) {  
            listImage = new ArrayList<>();
            listImage.add(new SelectItem(null, "Selecione"));
            listImage.add(new SelectItem("../resources/images/cafeteira.png", "Cafeteira"));
            listImage.add(new SelectItem("../resources/images/lampada-on.png", "Lâmpada ON"));
            listImage.add(new SelectItem("../resources/images/lampada-off.png", "Lâmpada OFF"));
            listImage.add(new SelectItem("../resources/images/luminosidade.png", "Luminosidade"));
            listImage.add(new SelectItem("../resources/images/pir.png", "Presença"));
            listImage.add(new SelectItem("../resources/images/pir2.png", "Presença2"));
            listImage.add(new SelectItem("../resources/images/pir3.png", "Presença3"));
            listImage.add(new SelectItem("../resources/images/potenciometro.png", "Potenciômetro"));
            listImage.add(new SelectItem("../resources/images/termometro.png", "Termômetro"));
            listImage.add(new SelectItem("../resources/images/termometro2.png", "Termômetro2"));
            listImage.add(new SelectItem("../resources/images/termometro3.png", "Termômetro3"));
            listImage.add(new SelectItem("../resources/images/umidade.png", "Umidade"));
            listImage.add(new SelectItem("../resources/images/ventilador.png", "Ventilador"));
        }
        return listImage;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.entity.DeviceImage;
import com.opendomotic.service.DeviceImageService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class ImageMB implements Serializable {
    
    @Inject 
    private DeviceImageService deviceImageService;
    
    private DeviceImage image = new DeviceImage();
    private boolean dialogVisible = false;
    
    public void create() {
        edit(new DeviceImage());
    }
    
    public void save() {
        deviceImageService.save(image);
        dialogVisible = image.getFileName() == null;
    }
        
    public void delete(DeviceImage image) {
        deviceImageService.delete(image);
    }
    
    public void edit(DeviceImage image) {
        this.image = image;
        this.dialogVisible = true;
    }

    public DeviceImage getEntity() {
        return image;
    }

    public List<DeviceImage> getListAll() {
        return deviceImageService.findAll();
    }
    
    public void handleFileUpload(FileUploadEvent event) throws IOException {  
        System.out.println("handle fileupload: "+event.getFile());
  
        FacesMessage msg = new FacesMessage("Sucesso", event.getFile().getFileName() + " foi carregado.");  
        FacesContext.getCurrentInstance().addMessage("teste", msg);
        
        String caminho = FacesContext.getCurrentInstance().getExternalContext()  
                .getRealPath("/resources/images/" + event.getFile().getFileName());  
  
        System.out.println(caminho);
        
        byte[] conteudo = event.getFile().getContents();  
        try (FileOutputStream fos = new FileOutputStream(caminho)) {
            fos.write(conteudo);
        }
        
        if (image.getId() != null) {
            image.setFileName(event.getFile().getFileName());
            deviceImageService.save(image);
        }
    } 

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }
    
}

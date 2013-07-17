/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb;

import com.opendomotic.model.entity.Environment;
import com.opendomotic.service.EnvironmentService;
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
 * @author Jaques
 */
@Named
@SessionScoped
public class EnvironmentMB implements Serializable {
    
    @Inject
    private EnvironmentService environmentService;
    
    private Environment environment = new Environment();
    private List<Environment> listEnvironment;
    private boolean dialogVisible = false;

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
        
        if (environment.getId() != null) {
            environment.setFileName(event.getFile().getFileName());
            environmentService.save(environment);
        }
    }
    
    public void create() {
        edit(new Environment());
    }
    
    public void save() {
        environmentService.save(environment);
        listEnvironment = null;
        dialogVisible = environment.getFileName() == null;
    }
    
    public void delete(Environment environment) {
        environmentService.delete(environment);
        listEnvironment = null;
    }
    
    public void edit(Environment environment) {
        this.environment = environment;
        this.dialogVisible = true;
    }
    
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    public List<Environment> getListEnvironment() {
        if (listEnvironment == null) {
            listEnvironment = environmentService.findAll();
        }
        return listEnvironment;
    }

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }
    
}

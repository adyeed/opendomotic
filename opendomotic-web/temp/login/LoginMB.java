package com.opendomotic.mb;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

@Named
@SessionScoped
public class LoginMB implements Serializable {

    private String usuario;
    private String senha;
    private boolean autenticado = false;

    public String login() throws ServletException {
        //TO-DO: JAAS
        autenticado = usuario.equals("user") && senha.equals("opendomotic");
        
        if (!autenticado) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Usuário ou senha incorreta!", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "login";
        } else {
            getSession().setAttribute("usuarioLogado", usuario);        
            return "user/index";
        }
    }

    public String logout() {
        getSession().removeAttribute("usuarioLogado");
        return "login";
    }

    private HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}

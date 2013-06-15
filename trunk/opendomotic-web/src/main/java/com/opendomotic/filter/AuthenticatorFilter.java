/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.filter;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jaques
 */
//@WebFilter(servletNames = {"Faces Servlet"})
public class AuthenticatorFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(AuthenticatorFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
    
        //Está autorizado se existir o atributo na session
        //OU página requisitada terminar com caminho raíz ou login.jsf
        boolean autorizado = 
                httpRequest.getSession().getAttribute("usuarioLogado") != null
                || httpRequest.getRequestURI().endsWith("login.jsf")
                || httpRequest.getRequestURI().contains("/javax.faces.resource/");
        
        if (autorizado) {
            chain.doFilter(request, response);
        } else {  
            String log = "Acesso Negado: " + httpRequest.getRequestURI();
            LOG.warning(log);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsf");
        }
    }

    @Override
    public void destroy() {
    }
    
}

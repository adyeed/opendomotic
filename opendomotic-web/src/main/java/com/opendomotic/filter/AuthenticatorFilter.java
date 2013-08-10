/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.filter;

import com.opendomotic.mb.HomeMB;
import java.io.IOException;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Jaques
 */
@WebFilter(servletNames = {"Faces Servlet"})
public class AuthenticatorFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(AuthenticatorFilter.class.getName());

    @Inject
    private HomeMB homeMB;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (homeMB != null && homeMB.getAdminLogged() == null) {
            LOG.info("logged in as admin");
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            homeMB.setAdminLogged(httpRequest.isUserInRole("admin"));
        }        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
    
}

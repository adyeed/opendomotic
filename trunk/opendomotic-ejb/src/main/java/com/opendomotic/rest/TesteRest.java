/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jaques
 */
@Path("/teste")
public class TesteRest {
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String doPutHtml() {
        return "Hello!";
    }
    
}

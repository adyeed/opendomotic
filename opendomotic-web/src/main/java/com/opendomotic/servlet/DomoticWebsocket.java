/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.servlet;

import com.opendomotic.service.BroadcastWebsocket;
import com.opendomotic.service.TimerService;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

/**
 *
 * @author jaques
 */
@WebServlet(urlPatterns = "/websocket")
public class DomoticWebsocket extends WebSocketServlet implements BroadcastWebsocket {
    
    private static final Logger LOG = Logger.getLogger(DomoticWebsocket.class.getName());    
    private List<DomoticInBound> connections = new ArrayList<>();

    @EJB
    private TimerService timerService;
    
    @PostConstruct
    public void initialize() {
        LOG.info("setBroadcastWebsocket on " + timerService);
        timerService.setBroadcastWebsocket(this);
    }
    
    @Override
    protected StreamInbound createWebSocketInbound(String string, HttpServletRequest hsr) {
        LOG.info(hsr.toString());
        return new DomoticInBound(this);
    }
    
    @Override
    public void sendBroadcast(String message) {
        for (DomoticInBound client : connections) {
            try {
                client.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
                String log = "Enviando mensagem: " + message;
                LOG.info(log);
            } catch (IOException ex) {
                LOG.severe(ex.toString());
            }
        }
    }

    public List<DomoticInBound> getConnections() {
        return connections;
    }
    
}

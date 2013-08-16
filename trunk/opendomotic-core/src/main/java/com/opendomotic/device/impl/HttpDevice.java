/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.device.impl;

import com.opendomotic.device.Device;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author Jaques
 */
public class HttpDevice implements Device<Integer> {

    private static final Logger LOG = Logger.getLogger(HttpDevice.class.getName());
    private String ip;
    private String path;

    @Override
    public Integer getValue() {
        try {
            HttpGet request = new HttpGet(getURL());
            HttpResponse response = new DefaultHttpClient().execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String value = rd.readLine();
            //LOG.log(Level.INFO, "Response={0} | {1}", new Object[] {value, url});
            return Integer.parseInt(value);
        } catch (IOException ex) {
            LOG.severe(ex.toString());
            return null;
        }
    }

    @Override
    public void setValue(Integer value) {
        try {
            long tempo = System.currentTimeMillis();
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);

            HttpGet request = new HttpGet(getURL() + "=" + value);
            HttpResponse response = httpClient.execute(request);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String responseStr = rd.readLine();
            LOG.log(Level.INFO, "Response={0} | {1} ms", new Object[] {responseStr, System.currentTimeMillis() - tempo});
        } catch (IOException ex) {
            LOG.severe(ex.toString());
        }
    }
    
    public String getURL() {
        return "http://" + ip + "/" + path;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "HttpDevice{" + "ip=" + ip + ", path=" + path + '}';
    }
    
}

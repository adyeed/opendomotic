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
    private static final boolean SHOW_LOG = false;
    private static final int DEFAULT_TIMEOUT = 2000;
    
    private String ip;
    private String path;

    @Override
    public Integer getValue() throws Exception {
        return Integer.parseInt(makeRequest(getURL()));
    }

    @Override
    public void setValue(Integer value) throws Exception {
        makeRequest(getURL() + "=" + value);
    }
    
    private HttpClient createHttpClient() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams httpParameters = httpClient.getParams();
        HttpConnectionParams.setTcpNoDelay(httpParameters, true);
        HttpConnectionParams.setConnectionTimeout(httpParameters, DEFAULT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, DEFAULT_TIMEOUT);        
        return httpClient;
    }
    
    private String readResponseLine(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        return rd.readLine();
    }
    
    private String makeRequest(String url) throws IOException {
        long time = System.currentTimeMillis();
        
        HttpGet request = new HttpGet(url);
        HttpResponse response = createHttpClient().execute(request);    
        String responseStr = readResponseLine(response);
        
        if (SHOW_LOG) {
            LOG.log(Level.INFO, "Response={0} | {1} ms | {2}", new Object[] {responseStr, System.currentTimeMillis() - time, url});
        }
        
        return responseStr;
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

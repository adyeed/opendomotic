package com.opendomotic.device.impl;

import com.opendomotic.device.Device;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author Jaques Claudino
 */
public class HttpDevice implements Device<Integer> {

    private static final Logger LOG = Logger.getLogger(HttpDevice.class.getName());    
    private static final boolean SHOW_LOG = false;
    private static final int DEFAULT_TIMEOUT = 2000;
    
    private String host;
    private String path;
    private String user;
    private String password;

    private HttpClient httpClient;
    
    @Override
    public Integer getValue() throws Exception {
        return Integer.parseInt(makeRequest(getURL()));
    }

    @Override
    public void setValue(Integer value) throws Exception {
        makeRequest(getURL() + "=" + value);
    }
    
    private HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();

            if (user != null && !user.isEmpty()) {
                UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, password);
                ((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
            }

            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);
            HttpConnectionParams.setConnectionTimeout(httpParameters, DEFAULT_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParameters, DEFAULT_TIMEOUT); 
            HttpConnectionParams.setSoKeepalive(httpParameters, false);
            HttpConnectionParams.setStaleCheckingEnabled(httpParameters, false);
        }        
        return httpClient;
    }
    
    private String readResponseLine(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        return rd.readLine();
    }
    
    private String makeRequest(String url) throws IOException {
        long time = System.currentTimeMillis();
        
        String responseStr = null;
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = getHttpClient().execute(request);    
            responseStr = readResponseLine(response);
        } finally {
            request.releaseConnection();
        }
        
        if (SHOW_LOG) {
            LOG.log(Level.INFO, "Response={0} | {1} | {2} ms", new Object[] {responseStr, url, System.currentTimeMillis() - time});
        }
        
        return responseStr;
    }
    
    public String getURL() {
        return "http://" + host + "/" + path;
    }

    public void setIp(String ip) {
        this.host = ip;
    }

    public void setHost(String host) {
        this.host = host;
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "HttpDevice{" + "host=" + host + ", path=" + path + '}';
    }
    
}

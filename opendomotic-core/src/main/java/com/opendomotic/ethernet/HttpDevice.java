/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.ethernet;

import com.opendomotic.api.Device;
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
public class HttpDevice implements Device {

    private static final Logger LOG = Logger.getLogger(HttpDevice.class.getName());
    private String name;
    private String url;

    public HttpDevice(String name, String ip, String path) {
        this.name = name;
        this.url = "http://" + ip + "/" + path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        try {
            HttpGet request = new HttpGet(url + "Read");
            HttpResponse response = new DefaultHttpClient().execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String value = rd.readLine();
            //LOG.log(Level.INFO, "{0} HttpResponse={1}", new Object[] {url, value});
            return Integer.parseInt(value);
        } catch (IOException ex) {
            LOG.severe(ex.toString());
            return null;
        }
    }

    @Override
    public void setValue(Object value) {
        try {
            long tempo = System.currentTimeMillis();
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);

            HttpGet request = new HttpGet(url + "=" + value);
            HttpResponse httpResponse = httpClient.execute(request);

            String line;
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            LOG.log(Level.INFO, "HttpResponse {0} | {1} ms", new Object[] {response.toString(), System.currentTimeMillis() - tempo});
        } catch (IOException ex) {
            LOG.severe(ex.toString());
        }
    }
}

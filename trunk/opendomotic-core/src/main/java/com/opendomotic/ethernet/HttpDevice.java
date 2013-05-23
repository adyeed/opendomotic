/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.ethernet;

import com.opendomotic.api.Device;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
            String log = url + " Response=" + value;
            LOG.info(log);
            return Integer.parseInt(value);            
        } catch (IOException ex) {
            LOG.severe(ex.toString());
            return -1;
        }
    }

    @Override
    public void setValue(Object value) {
        try {
            HttpGet request = new HttpGet(url + "=" + value);
            new DefaultHttpClient().execute(request);
            
            /*HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }*/
        } catch (IOException ex) {
            LOG.severe(ex.toString());
        }
    }
}

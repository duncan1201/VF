/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openide.util.Exceptions;
import org.openide.util.NetworkSettings;

/**
 *
 * @author dq
 */
public class HttpUtil {
    
    public static void closeQuitely(CloseableHttpClient client){
        try{
            client.close();
        }catch(Exception e){
        }
    }
    
    public static boolean hasSystemProxy(){
        List<Proxy> proxies = getSystemProxies();
        if(proxies == null || proxies.isEmpty()){
            return false;
        }else{
            Proxy proxy = proxies.get(0);
            if(proxy.type() == Proxy.Type.DIRECT){
                return false;
            }else{                
                return proxy.address() != null;
            }
        }
    }
    
    public static List<Proxy> getSystemProxies(){
        System.setProperty("java.net.useSystemProxies", "true");
        ProxySelector ps = ProxySelector.getDefault();
        
        List<Proxy> proxies = null;
        try {
            proxies = ps.select(new URI("http://www.google.com"));
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }        
        return proxies;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.data;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 * @author dq
 */
public class Data {
    public static String NM_001114 = "NM_001114.gb";
    public static String NM_001114_20_bp = "NM_001114_20_bp.gb";
    public static String NM_001114_modified = "NM_001114_modified.gb";
    public static String P_UC19 = "pUC19.gb";
    public static String P_UC19_MODIFIED = "pUC19_modified.gb";
    public static String P_UC19_MODIFIED_2 = "pUC19_modified_2.gb";
    public static String P_NEB193 = "pNEB193.gb";
    public static String M13KE = "M13KE.gb";
    public static String M13KE_MODIFIED = "M13KE_modified.gb";
    
    public static void main(String[] args) throws URISyntaxException{
        String useSystemProxy = System.getProperty("java.net.useSystemProxies");
        System.setProperty("java.net.useSystemProxies", "true");
        ProxySelector ps = ProxySelector.getDefault();
        System.out.println("Test:" + ps);
        System.out.println("Use system proxy:" + useSystemProxy);
        
        List<Proxy> proxies = ps.select(new URI("http://www.google.com"));
        for(Proxy p: proxies){
            InetSocketAddress socketAddress = (java.net.InetSocketAddress)p.address();
            System.out.println(p.type().toString() + "\t" + p.toString() + "\t" + socketAddress.getClass() + "\t");            
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.proxysetting.api;

import java.net.InetSocketAddress;
import java.net.Proxy;
import org.apache.http.HttpHost;

/**
 *
 * @author dq
 */
public class ProxySetting {
    public enum TYPE{
        NO_PROXY, SYSTEM_SETTING, MANUAL;
        
        public static TYPE getByName(String name){
            
            if(NO_PROXY.name().equals(name)){
                return NO_PROXY;
            }else if(SYSTEM_SETTING.name().equals(name)){
                return SYSTEM_SETTING;
            }else if(MANUAL.name().equals(name)){
                return MANUAL;
            }else{
                return null;
            }
        }
    };
    public enum PROXY_TYPE{
        HTTP, 
        SOCKETS;
    
        public static PROXY_TYPE getByName(String name){           
            if(HTTP.name().equals(name)){
                return HTTP;
            }else if(SOCKETS.name().equals(name)){
                return SOCKETS;
            }else{
                return null;
            }          
        }
    };
    private TYPE type;
    private PROXY_TYPE proxyType;
    private String hostName;
    private Integer port;
    // for manual proxy setting
    private Boolean authRequired;
    private String username;
    private String password;
    
    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public PROXY_TYPE getProxyType() {
        return proxyType;
    }

    public void setProxyType(PROXY_TYPE proxyType) {
        this.proxyType = proxyType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }  

    public Boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(Boolean authRequired) {
        this.authRequired = authRequired;
    }    
}
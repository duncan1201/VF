/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.proxysetting.api;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 *
 * @author dq
 */
public interface IProxyInternetService {
    ProxySetting getProxySetting();
    void saveProxySetting(ProxySetting proxySetting);
    HttpPost createHttpRequest(String uri);
    HttpGet createHttpGet(String uri);
    CloseableHttpClient createHttpClient();
    CloseableHttpClient createHttpClient(ProxySetting proxySetting);
    void displayConnProblem(boolean displayProxyButton);
    IProxyConfigPanel createProxyConfigPanel();
    void displayProxyConfigDialog();
    <T> T createHttpRequest(String uri, Class<T> clazz);
    <T> T createHttpRequest(String uri, ProxySetting proxySetting, Class<T> clazz);    
}

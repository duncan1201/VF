/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.exceptionhandler;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Dunqiang
 */
@ServiceProvider(service = IBugsubmissionService.class)
public class BugsubmissionService implements IBugsubmissionService {

    IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
    ExecutorService executorSvc = Executors.newFixedThreadPool(1);

    public void publish(String message, String stackTrace) {

        final CloseableHttpClient httpclient = internetSvc.createHttpClient();

        String uri = String.format("http://www.google.com?severity=severe&stackTrace", "");
        final HttpPost httpPost = internetSvc.createHttpRequest(uri);

        List<NameValuePair> nvps = new ArrayList<>();
        Date date = new Date();
        nvps.add(new BasicNameValuePair("dateTime", ""));
        nvps.add(new BasicNameValuePair("message", ""));
        nvps.add(new BasicNameValuePair("stackTrace", ""));

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
                Consts.UTF_8);
        httpPost.setEntity(formEntity);

        executorSvc.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    httpclient.execute(httpPost);
                } catch (IOException ex) {
                    //
                }
            }
        });
    }
}

package com.gas.das.core.command.type;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.HttpUtil;
import org.apache.http.Consts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openide.util.Lookup;

public class TypesCmd {
    //http://genome.cse.ucsc.edu:80/cgi-bin/das/hg19/

    //String uri = "http://www.ensembl.org/das/Homo_sapiens.GRCh37.reference/types";
    String uri = "http://genome.cse.ucsc.edu:80/cgi-bin/das/hg19/types";

    private void execute() {


        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpPost httpPost = internetSvc.createHttpRequest(uri);


        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
                    Consts.UTF_8);
            httpPost.setEntity(formEntity);
            HttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);

            FileHelper.toFile(new File(
                    "D:\\tmp\\dasobert\\types_response.xml"), result);
        } catch(java.net.UnknownHostException e){
            internetSvc.displayConnProblem(true);
        }catch (Exception e) {
            e.printStackTrace();
        } finally{
            HttpUtil.closeQuitely(httpclient);
        }
    }

    public static void main(String[] args) {
        TypesCmd c = new TypesCmd();
        c.execute();
        System.exit(0);
    }
}

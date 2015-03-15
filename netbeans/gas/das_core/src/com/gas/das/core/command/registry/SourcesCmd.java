package com.gas.das.core.command.registry;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.HttpUtil;
import org.apache.http.Consts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openide.util.Lookup;

public class SourcesCmd {

    public final static String URI = "http://www.dasregistry.org/das/sources";
    public final static String URI_1_6 = "http://www.dasregistry.org/das1.6/sources";
    public final static String URI_1_5 = "http://www.dasregistry.org/das1.5/sources";
    private String uri = "http://www.dasregistry.org/das/sources";

    public void execute() {
        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        
        //String uri = "http://www.dasregistry.org/das1.6/sources/";
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpPost httpPost = internetSvc.createHttpRequest(uri);

        //nvps.add(new BasicNameValuePair("capability", "sequence"));
        //nvps.add(new BasicNameValuePair("organism", "9606"));
        //nvps.add(new BasicNameValuePair("type", "Chromosome"));

        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
                    Consts.UTF_8);
            httpPost.setEntity(formEntity);
            HttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);

            FileHelper.toFile(
                    new File("D:\\tmp\\dasobert\\source_response.txt"), result);
        } catch(java.net.UnknownHostException e){
            internetSvc.displayConnProblem(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            HttpUtil.closeQuitely(httpclient);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SourcesCmd cmd = new SourcesCmd();
        cmd.execute();
    }
}

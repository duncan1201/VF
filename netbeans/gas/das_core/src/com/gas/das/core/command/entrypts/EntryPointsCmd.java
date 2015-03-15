package com.gas.das.core.command.entrypts;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

public class EntryPointsCmd {

    private String uri;
    private Integer start;
    private Integer end;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String execute() {

        if (start != null && start < 1) {
            throw new IllegalArgumentException("start ({0}) cannot be less than 1 ".format(start.toString()));
        }
        if (end != null && end < start) {
            throw new IllegalArgumentException("End ({0}) cannot be less than start({1}) ".format(end.toString(), start.toString()));
        }
        if (end != null && start == null) {
            throw new IllegalArgumentException("Both start and end need to be set or neither should be set");
        }
        if (end == null && start != null) {
            throw new IllegalArgumentException("Both start and end need to be set or neither should be set");
        }
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("URI cannot be empty");
        }

        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        
        String result = "";
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpPost httpPost = internetSvc.createHttpRequest(uri);

        nvps.add(new BasicNameValuePair("rows", "21-29"));

        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
                    Consts.UTF_8);
            httpPost.setEntity(formEntity);
            HttpResponse response = httpclient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
            //System.out.println(result);

            FileHelper.toFile(new File(
                    "D:\\tmp\\dasobert\\entry_points_response.txt"), result);


        } catch(java.net.UnknownHostException e){
            internetSvc.displayConnProblem(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            HttpUtil.closeQuitely(httpclient);
        }
        return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        EntryPointsCmd cmd = new EntryPointsCmd();
        String uri = "http://genome.cse.ucsc.edu:80/cgi-bin/das/hg19/entry_points";
        //String uri = "http://www.ensembl.org/das/Homo_sapiens.GRCh37.reference/entry_points";
        cmd.setUri(uri);
        String result = cmd.execute();
        System.out.println(result);
    }
}

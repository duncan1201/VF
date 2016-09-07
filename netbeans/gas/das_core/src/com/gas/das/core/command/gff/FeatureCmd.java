package com.gas.das.core.command.gff;

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
import com.gas.das.core.Seg;
import org.apache.http.Consts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openide.util.Lookup;

public class FeatureCmd {

    private String uri = "http://genome.cse.ucsc.edu:80/cgi-bin/das/hg19/features";
    private List<Seg> segments = new ArrayList<Seg>();
    private List<String> types = new ArrayList<String>();
    private List<String> categories = new ArrayList<String>();
    private Boolean categorize;

    public List<Seg> getSegments() {
        return segments;
    }

    public void setSegments(List<Seg> segments) {
        this.segments = segments;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Boolean getCategorize() {
        return categorize;
    }

    public void setCategorize(Boolean categorize) {
        this.categorize = categorize;
    }

    public void execute() {

        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList();
        HttpPost httpPost = internetSvc.createHttpRequest(uri);

        for (Seg seg : getSegments()) {
            nvps.add(new BasicNameValuePair("segment", seg.toString()));
        }
        for (String type : getTypes()) {
            nvps.add(new BasicNameValuePair("type", type));
        }
        if (categorize != null) {
            nvps.add(new BasicNameValuePair("categorize", categorize ? "yes" : "no"));
        }


        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
                    Consts.UTF_8);
            httpPost.setEntity(formEntity);
            HttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);

            FileHelper.toFile(new File(
                    "D:\\tmp\\dasobert\\features_response.xml"), result);
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
        FeatureCmd cmd = new FeatureCmd();
        cmd.execute();
    }
}

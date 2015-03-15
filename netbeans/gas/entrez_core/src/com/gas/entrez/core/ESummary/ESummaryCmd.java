package com.gas.entrez.core.ESummary;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import com.gas.entrez.core.ESummary.api.ESummaryResult;
import com.gas.entrez.core.ESummary.api.IESummaryCmd;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Consts;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IESummaryCmd.class)
public class ESummaryCmd implements IESummaryCmd {

    private String host = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi";
    private String db;
    private String tool;
    private String email;
    private String id;
    private String retstart;
    private String retmax;
    private String retmode;
    private String version = "1.0";

    private void validate() {
        if (getDb() == "") {
            throw new IllegalArgumentException("Database name can not be empty when doing ESummary request");
        }

        if (getId() == "") {
            throw new IllegalArgumentException("Id can not be empty when doing ESummary request");
        }
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public <T> T sendRequest(Class<T> retType) {
        validate();
        T ret = null;

        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpPost httpPost = internetSvc.createHttpRequest(host);

        nvps.add(new BasicNameValuePair("db", getDb()));
        nvps.add(new BasicNameValuePair("id", getId()));
        nvps.add(new BasicNameValuePair("retstart", getRetstart()));
        nvps.add(new BasicNameValuePair("retmax", getRetmax()));
        nvps.add(new BasicNameValuePair("version", getVersion()));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));


            HttpResponse response = httpclient.execute(httpPost);
            String s = EntityUtils.toString(response.getEntity());
            if(retType.isAssignableFrom(String.class)){
                ret = (T)s;
            }else if(retType.isAssignableFrom(ESummaryResult.class)){
                ret = (T)ESummaryResultParser.parse(s);
            }else{
                throw new IllegalArgumentException(String.format("Class '%s' not supported", retType.toString()));
            }
            

        } catch(UnknownHostException e){
            internetSvc.displayConnProblem(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            HttpUtil.closeQuitely(httpclient);
        }
        return ret;
    }

    @Override
    public String getDb() {
        return db == null ? "" : db;
    }

    @Override
    public void setDb(String db) {
        this.db = db;
    }

    @Override
    public String getTool() {
        return tool == null ? "" : tool;
    }

    @Override
    public void setTool(String tool) {
        this.tool = tool;
    }

    @Override
    public String getEmail() {
        return email == null ? "" : email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getId() {
        return id == null ? "" : id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRetstart() {
        return retstart == null ? "" : retstart;
    }

    @Override
    public void setRetstart(String retstart) {
        this.retstart = retstart;
    }

    @Override
    public String getRetmax() {
        return retmax == null ? "" : retmax;
    }

    @Override
    public void setRetmax(String retmax) {
        this.retmax = retmax;
    }

    @Override
    public String getRetmode() {
        return retmode == null ? "" : retmode;
    }

    @Override
    public void setRetmode(String retmode) {
        this.retmode = retmode;
    }
}
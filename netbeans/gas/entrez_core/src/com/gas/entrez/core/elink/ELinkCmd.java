package com.gas.entrez.core.elink;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import com.gas.entrez.core.elink.api.ELinkResult;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

public class ELinkCmd {

    private String host = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi";
    private String fromDb;
    private String toDb;
    private String linkName;
    private String[] ids;

    public String getFromDb() {
        return fromDb;
    }

    public void setFromDb(String fromDb) {
        this.fromDb = fromDb;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getToDb() {
        return toDb;
    }

    public void setToDb(String toDb) {
        this.toDb = toDb;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String... ids) {
        this.ids = ids;
    }

    private String getIdsAsString() {
        StringBuilder ret = new StringBuilder();        
        for(String id: ids){                    
            ret.append(id);
            ret.append(",");
        }

        return ret.substring(0, ret.length() - 1);
    }

    public <T> T sendRequest(Class<T> retType) {

        T ret = null;
        String result = null;
        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpPost httpPost = internetSvc.createHttpRequest(host);

        nvps.add(new BasicNameValuePair("dbfrom", getFromDb()));
        nvps.add(new BasicNameValuePair("db", getToDb()));
        nvps.add(new BasicNameValuePair("id", getIdsAsString()));
        nvps.add(new BasicNameValuePair("linkName", getLinkName()));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));


            HttpResponse response = httpclient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());

            if(retType.isAssignableFrom(String.class)){
                ret = (T)result;
            }else if(retType.isAssignableFrom(ELinkResult.class)){                
                ret = (T) ElinkResultParser.parse(result);
            }
        } catch(java.net.UnknownHostException e){
            internetSvc.displayConnProblem(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            HttpUtil.closeQuitely(httpclient);
        }
        return ret;
    }
}

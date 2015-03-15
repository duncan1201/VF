package com.gas.entrez.core.ESearch;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import com.gas.entrez.core.ESearch.api.IESearchCmdService;
import com.gas.entrez.core.ESearch.api.ESearchCmdResult;
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

@ServiceProvider(service = IESearchCmdService.class)
public class ESearchCmd implements IESearchCmdService {

    private String host = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi";
    private String db;
    private String term;
    private String tool;
    private String email;
    private String reldate;
    private String mindate;
    private String maxdate;
    private String datetype;
    private String retmode;
    private String rettype;
    private Integer retmax;
    private Integer retstart;

    public ESearchCmd() {
    }

    private void validate() {
        if (getDb().isEmpty()) {
            throw new IllegalArgumentException("Must set the database name.");
        }

        if (getTerm().isEmpty()) {
            throw new IllegalArgumentException("Must set the search term.");
        }
    }

    @Override
    public Integer getRetmax() {
        return retmax;
    }

    @Override
    public void setRetmax(Integer retmax) {
        this.retmax = retmax;
    }

    @Override
    public Integer getRetstart() {
        return retstart;
    }

    @Override
    public void setRetstart(Integer retstart) {
        this.retstart = retstart;
    }

    /**
     * @throws UnknownHostException it means <ul><li>the Internet connection is not present(usual case)<li>or the URL is wrong</ul>
     */
    private String sendRequest() throws UnknownHostException{
        validate();

        String ret = null;

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("db", getDb()));
        nvps.add(new BasicNameValuePair("term", getTerm()));
        nvps.add(new BasicNameValuePair("reldate", getReldate()));
        nvps.add(new BasicNameValuePair("mindate", getMindate()));
        nvps.add(new BasicNameValuePair("maxdate", getMaxdate()));
        nvps.add(new BasicNameValuePair("datetype", getDatetype()));
        nvps.add(new BasicNameValuePair("retmode", getRetmode()));
        nvps.add(new BasicNameValuePair("rettype", getRettype()));
        if (getRetstart() != null) {
            nvps.add(new BasicNameValuePair("retstart", getRetstart().toString()));
        }
        if (getRetmax() != null) {
            nvps.add(new BasicNameValuePair("retmax", getRetmax().toString()));
        }

        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        HttpPost httpPost = internetSvc.createHttpRequest(host);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse response = httpclient.execute(httpPost);
            ret = EntityUtils.toString(response.getEntity());
            if (ret == null) {
                System.out.println();
            }
        } catch(UnknownHostException uhe){
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
    public String getTerm() {
        return term == null ? "" : term;
    }

    @Override
    public void setTerm(String term) {
        this.term = term;
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
    public String getReldate() {
        return reldate == null ? "" : reldate;
    }

    @Override
    public void setReldate(String reldate) {
        this.reldate = reldate;
    }

    @Override
    public String getMindate() {
        return mindate == null ? "" : mindate;
    }

    @Override
    public void setMindate(String mindate) {
        this.mindate = mindate;
    }

    @Override
    public String getMaxdate() {
        return maxdate == null ? "" : maxdate;
    }

    @Override
    public void setMaxdate(String maxdate) {
        this.maxdate = maxdate;
    }

    @Override
    public String getDatetype() {
        return datetype == null ? "" : datetype;
    }

    @Override
    public void setDatetype(String datetype) {
        this.datetype = datetype;
    }

    @Override
    public String getRetmode() {
        return retmode == null ? "" : retmode;
    }

    @Override
    public void setRetmode(String retmode) {
        this.retmode = retmode;
    }

    @Override
    public String getRettype() {
        return rettype == null ? "" : rettype;
    }

    @Override
    public void setRettype(String rettype) {
        this.rettype = rettype;
    }

    @Override
    public <T> T sendRequest(Class<T> clazz) throws UnknownHostException {
        T ret = null;
        String retStr = sendRequest();
        if (clazz.isAssignableFrom(String.class)) {
            ret = (T) retStr;
        } else if (clazz.isAssignableFrom(ESearchCmdResult.class)) {
            ESearchCmdResult result = ESearchCmdResultParser.parse(retStr);
            ret = (T) result;
        } else {
            throw new IllegalArgumentException(String.format("%s not supported", clazz.toString()));
        }
        return ret;
    }
}
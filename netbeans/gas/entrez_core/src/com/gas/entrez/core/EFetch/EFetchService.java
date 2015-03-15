package com.gas.entrez.core.EFetch;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import com.gas.domain.ui.util.api.IEFetchService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.pubmed.PubmedArticle;



import com.gas.domain.core.pubmed.util.INCBIPubmedArticleSetParser;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEFetchService.class)
public class EFetchService implements IEFetchService {

    public static final Map<String, RETTYPE> DEFAULT_RETTYPES = new HashMap<String, RETTYPE>();
    public static final Map<String, RETMODE> DEFAULT_RETMODES = new HashMap<String, RETMODE>();

    static {
        DEFAULT_RETTYPES.put("nucleotide", RETTYPE.gb);
        DEFAULT_RETTYPES.put("protein", RETTYPE.gp);
        DEFAULT_RETTYPES.put("pubmed", RETTYPE._abstract);
        DEFAULT_RETTYPES.put("structure", RETTYPE.none);
        DEFAULT_RETTYPES.put("genome", RETTYPE.docsum);
    }

    static {
        DEFAULT_RETMODES.put("nucleotide", RETMODE.text);
        DEFAULT_RETMODES.put("protein", RETMODE.text);
        DEFAULT_RETMODES.put("pubmed", RETMODE.xml);
        DEFAULT_RETMODES.put("structure", RETMODE.text);
        DEFAULT_RETMODES.put("genome", RETMODE.xml);
    }
    
    private String db;
    private String ids;
    private String tool;
    private String email;
    private String retstart;
    private String retmax;
    private RETMODE retmode;
    private RETTYPE rettype;
    private String strand; // 1 = plus, 2 = minus
    private String seqStart;
    private String seqStop;
    /**
     * 0 - get the whole blob ;1 - get the bioseq for gi of interest (default in
     * Entrez) ;2 - get the minimal bioseq-set containing the gi of interest ; 3
     * - get the minimal nuc-prot containing the gi of interest; 4 - get the
     * minimal pub-set containing the gi of interest
     * */
    private String complexity;

    @Override
    public String getRettype() {
        if (rettype == null) {
            rettype = getDefaultRettype(getDb());
        }
        return rettype.toString();
    }

    private RETTYPE getDefaultRettype(String db) {
        if (DEFAULT_RETTYPES.containsKey(db.toLowerCase())) {
            return DEFAULT_RETTYPES.get(db.toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private RETMODE getDefaultRetmode(String db) {
        if (DEFAULT_RETMODES.containsKey(db.toLowerCase())) {
            return DEFAULT_RETMODES.get(db.toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void setRettype(RETTYPE rettype) {
        this.rettype = rettype;
    }

    @Override
    public String getStrand() {
        return strand;
    }

    @Override
    public void setStrand(String strand) {
        if (!strand.equals("1") && !strand.equals("2")) {
            throw new IllegalArgumentException(
                    "The value of strand must be 1 or 2(1 = plus, 2 = minus)");
        }
        this.strand = strand;
    }

    @Override
    public String getSeqStart() {
        return seqStart;
    }

    @Override
    public void setSeqStart(String seq_start) {
        this.seqStart = seq_start;
    }

    @Override
    public String getSeqStop() {
        return seqStop;
    }

    @Override
    public void setSeqStop(String seq_stop) {
        this.seqStop = seq_stop;
    }

    @Override
    public String getComplexity() {
        return complexity;
    }

    @Override
    public void setComplexity(String complexity) {
        if (!complexity.equals("1") && !complexity.equals("2")
                && !complexity.equals("3") && !complexity.equals("4")) {
            throw new IllegalArgumentException(
                    "Complexity must be 0,1,2,3, or 4");
        }
        this.complexity = complexity;
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
    public String getIds() {
        return ids == null ? "" : ids;
    }

    @Override
    public void setIds(String id) {
        this.ids = id;
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
        if (retmode == null) {
            retmode = getDefaultRetmode(getDb());        
        } 
        return retmode.toString();        
    }

    @Override
    public void setRetmode(RETMODE retmode) {
        this.retmode = retmode;
    }

    private void validate() {
        if (getDb().isEmpty()) {
            throw new IllegalArgumentException(
                    "Database name can not be empty when performing EFetch request");
        }

        if (getIds() == "") {
            throw new IllegalArgumentException(
                    "Id can not be empty when performing EFetch request");
        }

        validateRettypeAndRetmode();

    }

    private void validateRettypeAndRetmode() {
        if (getDb().equalsIgnoreCase("gene") && getRettype().equals("native")) {
            throw new IllegalArgumentException(
                    "rettype cannot be \"native\" for \"gene\" database.");
        }

        if (getRettype().equalsIgnoreCase("fasta") && getRetmode().equals("asn.1")) {
            throw new IllegalArgumentException(
                    "rettype cannot be \"fasta\" for \"asn.1\" ret mode.");
        }

    }

    @Override
    public <T> List<T> sendRequest(Class<T> clazz)  {
        List<T> ret = new ArrayList<T>();
        validate();


        String host = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi";

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();


        nvps.add(new BasicNameValuePair("db", getDb()));
        nvps.add(new BasicNameValuePair("id", getIds()));
        nvps.add(new BasicNameValuePair("tool", getTool()));
        nvps.add(new BasicNameValuePair("email", getEmail()));
        nvps.add(new BasicNameValuePair("retstart", getRetstart()));
        nvps.add(new BasicNameValuePair("retmax", getRetmax()));
        nvps.add(new BasicNameValuePair("retmode", getRetmode()));
        nvps.add(new BasicNameValuePair("rettype", getRettype()));

        IProxyInternetService proxyInternetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        CloseableHttpClient httpclient = proxyInternetSvc.createHttpClient();
        HttpPost httpPost = proxyInternetSvc.createHttpRequest(host);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse response = httpclient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());
            if (clazz.isAssignableFrom(String.class)) {
                ret.add((T) str);
            } else if (clazz.isAssignableFrom(AnnotatedSeq.class)) {
                ret = AnnotatedSeqParser.parse(str, new FlexGenbankFormat(), clazz);
            } else if(clazz.isAssignableFrom(PubmedArticle.class)){
                INCBIPubmedArticleSetParser parser = Lookup.getDefault().lookup(INCBIPubmedArticleSetParser.class);
                ret = parser.parse(str, clazz);
            }else {
                throw new IllegalArgumentException(String.format("class '%s' not supported", clazz.toString()));
            }

        } catch (java.net.UnknownHostException e){
            String title = "Cannot connect to the Internet";
            String msg = "No Internet Connection detected";
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(title);
            DialogDisplayer.getDefault().notify(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpUtil.closeQuitely(httpclient);
        }

        return ret;

    }

}

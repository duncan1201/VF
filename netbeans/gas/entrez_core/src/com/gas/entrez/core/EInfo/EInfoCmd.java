package com.gas.entrez.core.EInfo;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import com.gas.entrez.core.EInfo.api.EInfoResult;
import com.gas.entrez.core.EInfo.api.IEInfoCmd;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
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
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEInfoCmd.class)
public class EInfoCmd implements IEInfoCmd {

    private String db = "";
    private List<String> dbNames = new ArrayList<String>();
    private Map<String, EInfoResult> dbInfoMap = new HashMap<String, EInfoResult>();

    @Override
    public String getDb() {
        return db;
    }

    @Override
    public void setDb(String db) {
        this.db = db;
    }

    @Override
    public <T> T sendRequest(Class<T> clazz) {
        T ret = null;

        String host = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/einfo.fcgi";

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (!db.isEmpty()) {
            nvps.add(new BasicNameValuePair("db", getDb()));
        }

        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        HttpPost httpPost = internetSvc.createHttpRequest(host);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse response = httpclient.execute(httpPost);
            String r = EntityUtils.toString(response.getEntity());
            if (clazz.isAssignableFrom(String.class)) {
                ret = (T) r;
            } else if (clazz.isAssignableFrom(EInfoResult.class)) {
                ret = (T) EInfoResultParser.parse(r);
            } else {
                throw new IllegalArgumentException(String.format("class '%s' not supported", clazz.toString()));
            }

            //FileHelper.toFile(new File("D:\\tmp\\esearch_biosystems.xml"), ret);

        } catch(java.net.UnknownHostException e){
            internetSvc.displayConnProblem(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            HttpUtil.closeQuitely(httpclient);
        }
        return ret;
    }

    @Override
    public List<EInfoResult> getPreloaded() {
        File folder = InstalledFileLocator.getDefault().locate("modules/ext/ncbi_dbinfo", "com.gas.entrez.core", false);
        File[] files = folder.listFiles();

        for (File f : files) {
            String nameExt = f.getName();
            String name = nameExt.substring(0, nameExt.indexOf("."));

            if (!dbInfoMap.containsKey(name)) {
                EInfoResult result = EInfoResultParser.parse(f);
                dbInfoMap.put(name, result);
            }
        }

        return new ArrayList<EInfoResult>(dbInfoMap.values());
    }

    @Override
    public EInfoResult getPreloaded(String db) {
        EInfoResult ret = null;
        Iterator<String> itr = dbInfoMap.keySet().iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            if (str.equalsIgnoreCase(db)) {
                ret = dbInfoMap.get(str);
                break;
            }
        }

        if (ret == null) {
            File file = InstalledFileLocator.getDefault().locate("modules/ext/ncbi_dbinfo", "com.gas.entrez.core", false);
            File[] files = file.listFiles();

            for (File f : files) {
                String nameExt = f.getName();
                String name = nameExt.substring(0, nameExt.indexOf("."));
                if (name.equalsIgnoreCase(db)) {
                    ret = EInfoResultParser.parse(f);
                    dbInfoMap.put(name, ret);
                    break;
                }
            }
        }
        return ret;
    }
}

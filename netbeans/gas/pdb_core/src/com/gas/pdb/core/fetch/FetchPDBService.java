/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.pdb.core.fetch;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pdb.util.PDBParser;
import com.gas.pdb.core.fetch.api.IFetchPDBService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IFetchPDBService.class)
public class FetchPDBService implements IFetchPDBService{

    private final static String FORMAT = "http://www.rcsb.org/pdb/files/%s.pdb";
    
    @Override
    public <T> T sendRequest(String pdbId, Class<T> retType) {
        T ret = null;
        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        
        HttpGet httpGet = internetSvc.createHttpRequest(String.format(FORMAT, pdbId), HttpGet.class);

        try {
            HttpResponse response = httpclient.execute(httpGet);
            
            String str = EntityUtils.toString(response.getEntity());
            if (retType.isAssignableFrom(String.class)) {
                ret = (T)str;
            } else if (retType.isAssignableFrom(PDBDoc.class)) {
                ret =(T)PDBParser.parse(str);                
            } else {
                throw new IllegalArgumentException(String.format("class '%s' not supported", retType.toString()));
            }

        } catch (java.net.UnknownHostException e){
            internetSvc.displayConnProblem(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            HttpUtil.closeQuitely(httpclient);
        }

        return ret;
    }
    
}

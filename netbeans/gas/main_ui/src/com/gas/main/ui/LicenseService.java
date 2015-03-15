/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.appservice.api.IAppService;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
@SuppressWarnings("StaticNonFinalUsedInInitialization")
class LicenseService {

    static String HOST_LOCAL;
    static String HOST;
    static String REGIS_CODE_URL;

    private IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
    
    static {
        //http://localhost:8888
        HOST_LOCAL = HexUtil.toString("687474703a2f2f3132372e302e302e313a38383838");
        //http://cloningbuddy.appspot.com
        HOST = HexUtil.toString("687474703a2f2f636c6f6e696e6762756464792e61707073706f742e636f6d");
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        if(appService.isProduction()){
            REGIS_CODE_URL = String.format("%s%s", HOST, "/external/regisCode");
        }else{
            REGIS_CODE_URL = String.format("%s%s", HOST_LOCAL, "/external/regisCode");
        }
    }
    boolean basicMode;

    static LicenseService instance = new LicenseService();
    
    private LicenseService(){
    }
    
    static LicenseService getInstance(){
        return instance;
    }
    
    boolean isBasicMode() {
        return basicMode;
    }

    void setBasicMode(boolean basicMode) {
        this.basicMode = basicMode;
    }

    void saveSecretCode(String secretCode) {
        Preferences p = Preferences.userNodeForPackage(DoNotMoveOrDelete.class);
        p.put(MSG.SECRET_CODE, secretCode);
    }

    /**
     * @return static email [product name][product edition][max product release
     * date] [serial no]
     */
    String getSecretCode() {
        Preferences p = Preferences.userNodeForPackage(DoNotMoveOrDelete.class);
        try {
            String[] keys = p.keys();
            StringList strList = new StringList(keys);
            if (!strList.contains(MSG.SECRET_CODE)) {
                return null;
            } else {
                return p.get(MSG.SECRET_CODE, "");
            }
        } catch (BackingStoreException ex) {
            return null;
        }
    }

    /**
     * good or not_found
     */
    String releaseActivationCode(String serialNo) {
        String ret = null;
        String fingerprint = DiskUtil.getComputerFingerprint();
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();        
        HttpPost httpPost = createHttpPost();
        nvps.add(new BasicNameValuePair("cmd", "releaseStaticRegis"));
        nvps.add(new BasicNameValuePair("fingerprint", fingerprint));
        nvps.add(new BasicNameValuePair("serialNo", serialNo));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            HttpResponse response = httpclient.execute(httpPost);
            ret = EntityUtils.toString(response.getEntity());
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ioe) {
            internetSvc.displayConnProblem(true);            
        } finally{
            HttpUtil.closeQuitely(httpclient);
            return ret;
        }
    }

    /**
     * @return consumed [the activation code] or null
     */
    String activateStaticRegis(String regisHex) {
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        String ret = null;
        String fingerprint = DiskUtil.getComputerFingerprint();
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();        
        HttpPost httpPost = createHttpPost();
        nvps.add(new BasicNameValuePair("cmd", "activateStaticRegis"));
        nvps.add(new BasicNameValuePair("productName", appService.getAppName()));
        nvps.add(new BasicNameValuePair("version", appService.getCurrentVersion()));
        nvps.add(new BasicNameValuePair("os", appService.getOS().toString()));
        nvps.add(new BasicNameValuePair("regisCode", regisHex));
        nvps.add(new BasicNameValuePair("fingerprint", fingerprint));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            HttpResponse response = httpclient.execute(httpPost);
            ret = EntityUtils.toString(response.getEntity());           
        } catch (UnsupportedEncodingException ex) {
            //Exceptions.printStackTrace(ex);
            ex.printStackTrace();
        } catch (IOException ioe) {           
            internetSvc.displayConnProblem(true);
        } finally {
            HttpUtil.closeQuitely(httpclient);
            return ret;
        }
    }
    
    private HttpPost createHttpPost(){        
        HttpPost ret = internetSvc.createHttpRequest(REGIS_CODE_URL);
        return ret;
    }

    String getEmail() {
        String ret = null;
        String code = getSecretCode();
        if (code != null) {
            if (ActivationCode.isActivationCode(code)) {
                ActivationCode ac = ActivationCode.parse(code);
                ret = ac.getEmail();
            }
        }
        return ret;
    }

    String getActivationCodeFingerprint(String hex) {
        String ret = null;
        String clear = LicenseUtil.decrypt(hex);
        String[] splits = clear.split(" ");
        if (splits.length == 6) {
            ret = splits[5];
        }
        return ret;
    }

    Long getActivationCodeMaxReleaseTime(String hex) {
        String clear = LicenseUtil.decrypt(hex);
        String[] splits = clear.split(" ");
        Long ret = null;
        if (splits.length >= 6) {
            ret = Long.parseLong(splits[5]);
        }
        return ret;
    }

    /**
     * Possible outputs: NO_SUCH_USER, WRONG_PWD, NO_LICENSE, CREATED,
     * NO_ACTIVATABLE_LICENSE
     */
    String createStaticRegis(String email, String pwd) {
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        String ret = null;
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpPost httpPost = createHttpPost();
        nvps.add(new BasicNameValuePair("cmd", "createStaticRegis"));
        nvps.add(new BasicNameValuePair("email", email));
        nvps.add(new BasicNameValuePair("pwd", pwd));
        nvps.add(new BasicNameValuePair("productName", appService.getAppName()));
        nvps.add(new BasicNameValuePair("version", appService.getCurrentVersion()));
        nvps.add(new BasicNameValuePair("os", appService.getOS().name()));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            HttpResponse response = httpclient.execute(httpPost);
            ret = EntityUtils.toString(response.getEntity());
            System.out.println();
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ioe) {            
            internetSvc.displayConnProblem(true);
        } finally {
            HttpUtil.closeQuitely(httpclient);
            return ret;
        }
    }

    /**
     * @return possible values: ILLEGAL_ARGUMENT, NOT_ELIGIBLE, [success the
     * trial license] or null if no Internet connection
     */
    String createTrialLicense() {
        IProxyInternetService svc = Lookup.getDefault().lookup(IProxyInternetService.class);
        String ret = null;
        final String fingerprint = DiskUtil.getComputerFingerprint();
        CloseableHttpClient httpClient = svc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpPost httpPost = createHttpPost();
        nvps.add(new BasicNameValuePair("cmd", "createTrialLicense"));
        nvps.add(new BasicNameValuePair("fingerprint", fingerprint));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            ret = EntityUtils.toString(response.getEntity());
        } catch (UnsupportedEncodingException ex) {
            //Exceptions.printStackTrace(ex);
        } catch(UnknownHostException ue){
            //Exceptions.printStackTrace(ue);
        } catch (IOException e) {            
            svc.displayConnProblem(true);
        } finally{
            HttpUtil.closeQuitely(httpClient);
            return ret;
        }
        
    }

    boolean hasSecretCode() {
        String code = getSecretCode();
        return code != null && !code.isEmpty();
    }

    void deleteSecretKey() {
        Preferences p = Preferences.userNodeForPackage(DoNotMoveOrDelete.class);
        p.remove(MSG.SECRET_CODE);
    }

    String getLegalSecretCode() {
        String ret = getSecretCode();
        if (ret != null && !ret.isEmpty()) {
            if (TrialLicense.isTrialLicense(ret) || ActivationCode.isActivationCode(ret) || FreeAcademicLicense.isFreeAcademicLicense(ret)) {
                return ret;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}

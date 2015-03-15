/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update.service;

import com.gas.update.service.api.SoftProductList;
import com.gas.common.ui.appservice.api.IAppService;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.HttpUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.update.service.api.ISoftProductService;
import com.gas.update.service.api.SoftProduct;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
@ServiceProvider(service = ISoftProductService.class)
public class SoftProductService implements ISoftProductService {

    static final String checkAtStartup = "checkAtStartup";
    static final String HOST_LOCAL = "http://localhost:8888";
    static final String HOST = "http://www.vectorfriends.com";
    static String URL = "";
    static final IAppService appService = Lookup.getDefault().lookup(IAppService.class);

    static {
        if (appService.isProduction()) {
            URL = String.format("%s/softProduct", HOST);
        } else {
            URL = String.format("%s/softProduct", HOST_LOCAL);
        }
    }

    void displayResult(SoftProductList list, boolean quiteOnNoUpdates) {

        if (list != null && !list.isEmpty()) {
            SoftProduct sp = list.getLatestEdition();
            NewReleasePanel newReleasePanel = new NewReleasePanel(sp);
            String title = "New release of VectorFriends available";
            FontMetrics fm = FontUtil.getDefaultFontMetrics();
            Object[] options = {DialogDescriptor.CLOSED_OPTION};
            DialogDescriptor dd = new DialogDescriptor(newReleasePanel,
                    title,
                    true,
                    options,
                    DialogDescriptor.CLOSED_OPTION,
                    DialogDescriptor.DEFAULT_ALIGN,
                    null,
                    null);
            UIUtil.setPreferredWidth(newReleasePanel, fm.stringWidth(title) * 7 / 5);
            DialogDisplayer.getDefault().notify(dd);

            ISoftProductService s = Lookup.getDefault().lookup(ISoftProductService.class);
            s.setCheckAtStartup(!newReleasePanel.getDonotshow().isSelected());
        } else {
            if (!quiteOnNoUpdates) {
                DialogDescriptor.Message m = new DialogDescriptor.Message("<html>Thanks for checking! <br/><br/>Your VectorFriends is up to update!</html>", DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle("Check for updates");

                DialogDisplayer.getDefault().notify(m);
            }
        }

    }

    @Override
    public void checkNewRelease(boolean showProgress, boolean quiteOnNoUpdates) {
        if (showProgress) {
            Frame frame = WindowManager.getDefault().getMainWindow();
            ProgressHelper.showProgressDialogAndRun(frame, "Checking remote server...", new ProgRunnable() {
                SoftProductList resp;

                @Override
                public void run(ProgressHandle handle) {
                    handle.setIndeterminate(true);
                    handle.progress("Checking remote server...");
                    resp = getOSCompatibleProducts();
                }

                @Override
                public void done(ProgressHandle handle) {
                    displayResult(resp, false);
                }
            }, "Checking for Updates");
        } else {
            CheckNewReleaseWorker worker = new CheckNewReleaseWorker(quiteOnNoUpdates);
            worker.execute();
        }
    }

    @Override
    public boolean isCheckAtStartup() {
        Preferences prefs = NbPreferences.forModule(ISoftProductService.class).node(ISoftProductService.class.getSimpleName());
        return prefs.getBoolean(checkAtStartup, true);
    }

    @Override
    public void setCheckAtStartup(boolean check) {
        Preferences prefs = NbPreferences.forModule(ISoftProductService.class).node(ISoftProductService.class.getSimpleName());
        prefs.putBoolean(checkAtStartup, check);
    }

    public SoftProductList getOSCompatibleProducts() {

        SoftProductList list = getAllProducts();
        if (list == null) {
            return null;
        }
        String os;
        if (Utilities.isWindows()) {
            os = CNST.OS_WINDOWS;
        } else {
            throw new UnsupportedOperationException();
        }

        String bit;
        if (CommonUtil.is64bit()) {
            bit = "64";
        } else {
            bit = "32";
        }
        Boolean win64 = Utilities.isWindows() && CommonUtil.is64bit() ? true : null;
        Boolean win32 = Utilities.isWindows() && !CommonUtil.is64bit() ? true : null;
        Boolean mac = Utilities.isMac() && !CommonUtil.is64bit() ? true : null;
        return list.filter(appService.getAppName(), win64, win32, mac, appService.getCurrentVersion());
    }

    private SoftProductList getAllProducts() {
        SoftProductList ret = null;

        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();        
        HttpPost httpPost = internetSvc.createHttpRequest(URL);

        nvps.add(new BasicNameValuePair("cmd", "getAll"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            HttpResponse response = httpclient.execute(httpPost);
            String resp = EntityUtils.toString(response.getEntity());
            Object tmp = JSONValue.parse(resp);
            ret = new SoftProductList((JSONArray) tmp);
        } catch(java.net.UnknownHostException ue){
            //internetSvc.displayConnProblem(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpUtil.closeQuitely(httpclient);
            return ret;
        }
    }

    @Override
    public int compareEdition(SoftProduct sp1, SoftProduct sp2) {        
        SoftProduct.Sorter sorter = new SoftProduct.Sorter();
        int ret = sorter.compare(sp1, sp2);
        return ret;
    }

    @Override
    public int compareEdition(String e1, String e2) {
        SoftProduct.Sorter sorter = new SoftProduct.Sorter();
        int ret = sorter.compare(e1, e2);
        return ret;
    }
}

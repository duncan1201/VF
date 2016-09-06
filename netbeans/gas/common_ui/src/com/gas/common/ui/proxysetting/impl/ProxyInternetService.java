/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.proxysetting.impl;

import com.gas.common.ui.proxysetting.api.IProxyConfigPanel;
import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.proxysetting.api.ProxySetting;
import com.gas.common.ui.util.HttpUtil;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IProxyInternetService.class)
public class ProxyInternetService implements IProxyInternetService {

    private static final String TYPE = "type";
    private static final String PROXY_TYPE = "proxy_type";
    private static final String HOST_NAME = "host_name";
    private static final String PORT = "port";
    private static final String AUTH_REQUIRED = "auth_required";
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";

    @Override
    public ProxySetting getProxySetting() {
        ProxySetting ret = new ProxySetting();

        Preferences pref = getPreferences();
        String type = pref.get(TYPE, ProxySetting.TYPE.NO_PROXY.name());
        String proxyType = pref.get(PROXY_TYPE, ProxySetting.PROXY_TYPE.HTTP.name());
        String hostName = pref.get(HOST_NAME, "");
        int port = pref.getInt(PORT, 80);
        Boolean authRequired = pref.getBoolean(AUTH_REQUIRED, Boolean.FALSE);
        String username = pref.get(USER_NAME, "");
        String pwd = pref.get(PASSWORD, "");

        ProxySetting.TYPE _type = ProxySetting.TYPE.getByName(type);
        ProxySetting.PROXY_TYPE _proxyType = ProxySetting.PROXY_TYPE.getByName(proxyType);

        ret.setType(_type);
        ret.setProxyType(_proxyType);
        ret.setHostName(hostName);
        ret.setPort(port);
        // for manual proxy only
        ret.setAuthRequired(authRequired);
        ret.setUsername(username);
        ret.setPassword(pwd);

        return ret;
    }

    private Preferences getPreferences() {
        Preferences ret = NbPreferences.forModule(ProxyInternetService.class).node(ProxyInternetService.class.getSimpleName());
        return ret;
    }

    @Override
    public void saveProxySetting(ProxySetting proxySetting) {
        Preferences pref = getPreferences();

        ProxySetting.TYPE type = proxySetting.getType();
        ProxySetting.PROXY_TYPE proxyType = proxySetting.getProxyType();
        String hostName = proxySetting.getHostName();
        Integer port = proxySetting.getPort();

        pref.put(TYPE, type.name());
        if (proxyType != null) {
            pref.put(PROXY_TYPE, proxyType.name());
        } else {
            pref.remove(PROXY_TYPE);
        }
        if (hostName != null) {
            pref.put(HOST_NAME, hostName);
        } else {
            pref.remove(HOST_NAME);
        }
        if (port != null) {
            pref.putInt(PORT, port);
        } else {
            pref.remove(PORT);
        }
        if (proxySetting.isAuthRequired() != null) {
            pref.putBoolean(AUTH_REQUIRED, proxySetting.isAuthRequired());
        } else {
            pref.remove(AUTH_REQUIRED);
        }
        if (proxySetting.getUsername() != null) {
            pref.put(USER_NAME, proxySetting.getUsername());
        } else {
            pref.remove(USER_NAME);
        }
        if (proxySetting.getPassword() != null) {
            pref.put(PASSWORD, proxySetting.getPassword());
        } else {
            pref.remove(PASSWORD);
        }
    }

    @Override
    public HttpPost createHttpRequest(String uri) {
        return createHttpRequest(uri, HttpPost.class);
    }
    
    public HttpGet createHttpGet(String uri) {
        return createHttpRequest(uri, HttpGet.class);
    }

    @Override
    public <T> T createHttpRequest(String uri, Class<T> clazz) {
        return createHttpRequest(uri, getProxySetting(), clazz);
    }

    @Override
    public CloseableHttpClient createHttpClient() {
        return createHttpClient(getProxySetting());
    }

    @Override
    public void displayConnProblem(boolean displayProxyButton) {
        String title = "Internet Connection Problem";
        ConnProblemPanel connPanel = new ConnProblemPanel(displayProxyButton);
        DialogDescriptor dd = new DialogDescriptor(connPanel, title);
        DialogDisplayer.getDefault().notify(dd);        
    }

    @Override
    public CloseableHttpClient createHttpClient(ProxySetting proxySetting) {
        CloseableHttpClient ret = null;
        if (proxySetting.getType() == ProxySetting.TYPE.NO_PROXY || proxySetting.getType() == ProxySetting.TYPE.SYSTEM_SETTING) {
            ret = HttpClients.createDefault();
        } else if (proxySetting.getType() == ProxySetting.TYPE.MANUAL) {
            proxySetting.getHostName();
            Boolean isAuthRequired = proxySetting.isAuthRequired();
            if (isAuthRequired) {
                String host = proxySetting.getHostName();
                Integer port = proxySetting.getPort();
                String userName = proxySetting.getUsername();
                String pwd = proxySetting.getPassword();

                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(userName, pwd));

                ret = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
            } else {
                ret = HttpClients.createDefault();
            }
        }
        return ret;
    }

    @Override
    public <T> T createHttpRequest(String uri, ProxySetting ps, Class<T> clazz) {
        if (clazz.getName().equals(HttpPost.class.getName()) && clazz.getName().equals(HttpGet.class.getName())) {
            throw new IllegalArgumentException(String.format("The return type $s not supported", clazz.getName()));
        }
        T ret = null;
        if (clazz.getName().equals(HttpPost.class.getName())) {
            ret = (T) new HttpPost(uri);
        } else if (clazz.getName().equals(HttpGet.class.getName())) {
            ret = (T) new HttpGet(uri);
        }
        if (ps == null) {
            ps = getProxySetting();
        }
        if (ps == null) {
            throw new NullPointerException("ProxySetting cannot be null");
        }
        String hostName = null;
        Integer port = null;
        String scheme = "http";
        if (ps.getType() == ProxySetting.TYPE.MANUAL || ps.getType() == ProxySetting.TYPE.SYSTEM_SETTING) {
            if (ps.getType() == ProxySetting.TYPE.MANUAL) {
                hostName = ps.getHostName();
                port = ps.getPort();

            } else if (ps.getType() == ProxySetting.TYPE.SYSTEM_SETTING) {
                List<Proxy> proxies = HttpUtil.getSystemProxies();
                if (!proxies.isEmpty()) {
                    Proxy proxy = proxies.get(0);
                    if (proxy.type() != Proxy.Type.DIRECT) {
                        InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
                        if (socketAddress != null) {
                            hostName = socketAddress.getHostName();
                            port = socketAddress.getPort();
                        }
                        Proxy.Type pType = proxy.type();
                        if (pType == Proxy.Type.HTTP) {
                            scheme = "http";
                        } else if (pType == Proxy.Type.SOCKS) {
                            throw new UnsupportedOperationException("Socks proxy not supported yet");
                        }
                    }
                }
            }
            HttpHost proxyHost;
            if (hostName != null && port != null) {
                proxyHost = new HttpHost(hostName, port, scheme);
                RequestConfig config = RequestConfig.custom().setProxy(proxyHost).build();
                ((HttpRequestBase) ret).setConfig(config);
            }

        } else if (ps.getType() == ProxySetting.TYPE.NO_PROXY) {
            // nothing to do here
        } else {
            throw new IllegalStateException(String.format("Unknown proxy setting type: %s", ps.getType()));
        }
        return ret;
    }
    
    @Override
    public void displayProxyConfigDialog(){
        String title = "Internet Proxy Setting";
        IProxyConfigPanel p = createProxyConfigPanel();
        DialogDescriptor dd = new DialogDescriptor((JComponent)p, title);
        Object answer = DialogDisplayer.getDefault().notify(dd);
        if(answer.equals(DialogDescriptor.OK_OPTION)){
            ProxySetting ps = p.getProxySetting();
            saveProxySetting(ps);
        }
    }
    
    @Override
    public IProxyConfigPanel createProxyConfigPanel(){
        IProxyConfigPanel ret = new ProxyConfigPanel();
        return ret;
    }

    public void test() throws URISyntaxException {
        String useSystemProxy = System.getProperty("java.net.useSystemProxies");
        System.setProperty("java.net.useSystemProxies", "true");
        ProxySelector ps = ProxySelector.getDefault();
        System.out.println("Test:" + ps);
        System.out.println("Use system proxy:" + useSystemProxy);

        List<Proxy> proxies = ps.select(new URI("http://www.google.com"));
        for (Proxy p : proxies) {
            InetSocketAddress socketAddress = (java.net.InetSocketAddress) p.address();
            System.out.println(p.type().toString() + "\t" + p.toString() + "\t" + socketAddress.getClass() + "\t");
        }
    }
}

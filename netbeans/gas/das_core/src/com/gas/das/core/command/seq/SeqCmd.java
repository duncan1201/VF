package com.gas.das.core.command.seq;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.http.Consts;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.openide.util.Lookup;

public class SeqCmd {

    public static String DOWNLOADED_CHANGED_PROPERTY = "PROGRESS_CHANGED_PROPERTY";
    private String host;//= "http://www.ensembl.org/das/Homo_sapiens.GRCh37.reference";
    Map<String, String> dsnMap = new HashMap<String, String>();
    private int threadPoolSize = 10;
    private int segSize = 8000;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Long totalLength = 0l;
    // for internal use
    private List<String> subDsnTargets = new ArrayList<String>();
    private Long downloadedLength = 0l;
    Object downloadedLengthLock = new Object();

    public Long getTotalLength() {
        return totalLength;
    }

    public Map<String, String> getDsnMap() {
        return dsnMap;
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getSegSize() {
        return segSize;
    }

    public void setSegSize(int segSize) {
        this.segSize = segSize;
    }

    public void execute() {
        if (dsnMap.size() == 0) {
            throw new IllegalArgumentException("NO DSNs");
        }
        if (host == null || host.length() == 0) {
            throw new IllegalArgumentException("Host cannot be empty");
        }
        Iterator<String> keys = dsnMap.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();
            String value = dsnMap.get(key);
            String[] range = value.split(",");
            if (range.length == 1) {
                subDsnTargets.add(value);
            } else {
                int start = Integer.parseInt(range[0]);
                int stop = Integer.parseInt(range[1]);
                totalLength += (stop - start + 1);
                subDsnTargets.addAll(getDsnSubList(key, start, stop));
            }
        }

        ExecutorService exeService = Executors.newFixedThreadPool(threadPoolSize);
        Collection<Callable<String>> callables = new ArrayList<Callable<String>>();

        String uri = host;
        if (!uri.endsWith("/")) {
            uri += "/";
        }
        uri += "sequence";

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
        cm.setMaxTotal(10);

        for (String dsnTarget : subDsnTargets) {

            callables.add(new SeqCmdCallable(dsnTarget, uri, cm));
        }
        try {
            List<Future<String>> futures = exeService.invokeAll(callables);
            cm.shutdown();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private List<String> getDsnSubList(String dsnName, int start, int stop) {
        List<String> dsnParam = new ArrayList<String>();
        for (int i = start - 1; i < stop; i = i + segSize) {
            int tmpStart = i + 1;
            int tmpStop = Math.min(stop, i + segSize);

            dsnParam.add(dsnName + ":" + tmpStart + "," + tmpStop);

        }
        return dsnParam;
    }

    private class SeqCmdCallable implements Callable<String> {

        private String dsnTarget;
        private String uri;

        public SeqCmdCallable(String dsnTarget, String uri, ClientConnectionManager cm) {
            this.dsnTarget = dsnTarget;
            this.uri = uri;
        }

        @Override
        public String call() throws Exception {
            List list = Arrays.asList(new String[]{dsnTarget});
            return _execute(list, uri);
        }
    }

    private String _execute(List<String> dsnTargets, String uri) {
        String result = null;
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        int seqSize = 0;
        for (String dsnTarget : dsnTargets) {
            String[] ranges = dsnTarget.split(":")[1].split(",");
            int start = Integer.parseInt(ranges[0]);
            int end = Integer.parseInt(ranges[1]);
            seqSize += (end - start + 1);
            nvps.add(new BasicNameValuePair("segment", dsnTarget));
        }
        IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
        
        CloseableHttpClient httpclient = internetSvc.createHttpClient();
        HttpPost httpPost = internetSvc.createHttpRequest(uri);

        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
                    Consts.UTF_8);
            httpPost.setEntity(formEntity);

            HttpResponse response = httpclient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());

            synchronized (downloadedLengthLock) {
                Long old = downloadedLength;
                downloadedLength += seqSize;
                PropertyChangeEvent event = new PropertyChangeEvent(SeqCmd.this, DOWNLOADED_CHANGED_PROPERTY, old, downloadedLength);
                propertyChangeSupport.firePropertyChange(event);
            }
        } catch(java.net.UnknownHostException e){
            internetSvc.displayConnProblem(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            HttpUtil.closeQuitely(httpclient);
        }
        return result;
    }

    public static void main(String[] args) {
        final Long startTime = System.currentTimeMillis();
        SeqCmd c = new SeqCmd();
        c.getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                SeqCmd seqCmd = (SeqCmd) e.getSource();
                Long totalLength = seqCmd.getTotalLength();
                Long downloaded = (Long) e.getNewValue();
                //System.out.println("downloaded="+downloaded);
                double percent = 100.0 * downloaded / totalLength;
                System.out.println("downloaded=" + percent + "%");
                if (downloaded == totalLength) {
                    Long t = System.currentTimeMillis();
                    System.out.println("Total time=" + (t - startTime));
                }
            }
        });
        c.setHost("http://www.ensembl.org/das/Homo_sapiens.GRCh37.reference");
        c.getDsnMap().put("X", "1,170560"); // 155270560

        c.execute();
        System.exit(0);
    }
}

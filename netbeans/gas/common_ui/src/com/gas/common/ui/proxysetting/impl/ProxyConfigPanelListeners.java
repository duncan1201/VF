/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.proxysetting.impl;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.proxysetting.api.ProxySetting;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class ProxyConfigPanelListeners {

    public static class ProxyOptionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent src = (JComponent) e.getSource();
            ProxyConfigPanel generalPanel = UIUtil.getParent(src, ProxyConfigPanel.class);
            if (generalPanel == null) {
                return;
            }
            String cmd = e.getActionCommand();
            generalPanel.updateManualProxyPanelEnablement();

            if (cmd.equals(ProxyConfigPanel.CMD_NO_PROXY)) {
            } else if (cmd.equals(ProxyConfigPanel.CMD_SYSTEM_PROXY)) {
            } else if (cmd.equals(ProxyConfigPanel.CMD_MANUAL_PROXY)) {
            }
            generalPanel.validateInput();
        }
    }

    public static class ManualProxyAuthListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComponent src = (JComponent) e.getSource();
            ProxyConfigPanel generalPanel = UIUtil.getParent(src, ProxyConfigPanel.class);
            if (generalPanel == null) {
                return;
            }
            generalPanel.updateManualProxyPanelEnablement();
            generalPanel.validateInput();
        }
    }

    static class DocListener implements DocumentListener {

        WeakReference<ProxyConfigPanel> ref;

        DocListener(ProxyConfigPanel generalPanel) {
            if (generalPanel == null) {
                throw new IllegalArgumentException("GeneralPanel cannot be null");
            }
            ref = new WeakReference<ProxyConfigPanel>(generalPanel);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateInput();
        }

        private void validateInput() {
            if (ref != null && ref.get() != null) {
                ref.get().validateInput();
            }
        }
    }

    static class TxtFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent src = (JComponent) e.getSource();
            ProxyConfigPanel generalPanel = UIUtil.getParent(src, ProxyConfigPanel.class);
            if (generalPanel == null) {
                return;
            }
            generalPanel.validateInput();
        }
    }

    static class TestBtnListener implements ActionListener {

        IProxyInternetService svc = Lookup.getDefault().lookup(IProxyInternetService.class);
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent src = (JComponent) e.getSource();
            ProxyConfigPanel panel = UIUtil.getParent(src, ProxyConfigPanel.class);
            if (panel == null) {
                return;
            }
            
            ProxySetting pSetting = panel.getProxySetting();
            HttpClient httpClient = svc.createHttpClient(pSetting);
            HttpGet httpGet = svc.createHttpRequest("http://www.google.com", pSetting, HttpGet.class);
            
            panel.getTestResultLabel().setIcon(ImageHelper.createImageIcon(ImageNames.CIRCLE_BALL_16));
            panel.getTestResultLabel().setText("");
            
            TestConnWorker worker = new TestConnWorker(panel, httpClient, httpGet);
            worker.execute();
        }

        private class TestConnWorker extends SwingWorker {

            private WeakReference<ProxyConfigPanel> ref;
            private HttpClient httpClient;
            private HttpGet httpGet;
            private boolean success;

            TestConnWorker(ProxyConfigPanel panel, HttpClient httpClient, HttpGet httpGet) {
                ref = new WeakReference<ProxyConfigPanel>(panel);
                this.httpClient = httpClient;
                this.httpGet = httpGet;
            }

            @Override
            protected Object doInBackground() throws Exception {
                ref.get().getTestConnBtn().setEnabled(false);
                try {
                    httpClient.execute(httpGet);
                    success = true;
                } catch (IOException e) {
                    success = false;
                } catch (Exception e) {
                    success = false;
                }
                return null;
            }

            @Override
            public void done() {
                if (success) {
                    ref.get().getTestResultLabel().setForeground(ColorCnst.DARTMOUTH_GREEN);
                    ref.get().getTestResultLabel().setText("Success");
                } else {
                    ref.get().getTestResultLabel().setForeground(Color.RED);
                    ref.get().getTestResultLabel().setText("Fail");
                }
                ref.get().getTestResultLabel().setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                ref.get().getTestConnBtn().setEnabled(true);
            }
        }
    }
}

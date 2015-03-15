/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update.service;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.update.service.api.SoftProduct;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author dq
 */
public class NewReleasePanel extends JPanel {

    final static String TITLE_FT = "<html><b>%s %s is available for download</b></html>";
    private JXHyperlink releaseNotesURL;
    private JXHyperlink installersURL;
    private JCheckBox donotshow;
    private SoftProduct sp;

    NewReleasePanel(SoftProduct sp) {
        this.sp = sp;
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        JPanel titlePanel = createTitlePanel();
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        FontMetrics fm = FontUtil.getDefaultFontMetrics();
        c.insets = new Insets(fm.getHeight(),0,fm.getHeight(),0);
        add(titlePanel, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        JPanel downloadPanel = createDownloadPanel(sp);
        add(downloadPanel, c);

        String releaseNotesURLStr = sp.getReleaseNotesURL();
        if (releaseNotesURLStr != null && !releaseNotesURLStr.isEmpty()) {
            JPanel releaseNotesPanel = createReleaseNotesPanel(sp);
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.gridx = 0;
            add(releaseNotesPanel, c);
        }

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.insets = new Insets(fm.getHeight(), 0, 0, 0);
        donotshow = new JCheckBox("Do not show again");
        add(donotshow, c);

        hookupListeners();
    }

    private JPanel createTitlePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        JLabel label = new JLabel(String.format(TITLE_FT, sp.getName(), sp.getEdition()));
        ret.add(label, c);
        
        c = new GridBagConstraints();
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JSeparator separator = new JSeparator();
        ret.add(separator, c);
        return ret;
    }

    private JPanel createDownloadPanel(final SoftProduct sp) {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();        
        c.gridy = 0;
        JLabel label = new JLabel("Click");
        ret.add(label, c);

        c = new GridBagConstraints();
        c.gridy = 0;
        installersURL = new JXHyperlink();
        installersURL.setFocusable(false);        
        installersURL.setAction(new AbstractAction(" here ") {
            @Override
            public void actionPerformed(ActionEvent e) {
                //CommonUtil.browse("http://www.vectorfriends.com/downloads.html");
                final String installerUrl = sp.getInstallerURL();                
                CommonUtil.browse(installerUrl);                
            }
        });
        ret.add(installersURL, c);

        c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        label = new JLabel("to download");
        ret.add(label, c);
        return ret;
    }

    private JPanel createReleaseNotesPanel(final SoftProduct sp) {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel("Read what's new in");
        ret.add(label, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        releaseNotesURL = new JXHyperlink();
        releaseNotesURL.setFocusable(false);        
        releaseNotesURL.setAction(new AbstractAction(" the release notes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse(sp.getReleaseNotesURL());
            }
        });
        ret.add(releaseNotesURL, c);

        return ret;
    }

    public JCheckBox getDonotshow() {
        return donotshow;
    }

    private void hookupListeners() {
        installersURL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse(sp.getInstallerURL());
            }
        });

        if (releaseNotesURL != null) {
            releaseNotesURL.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CommonUtil.browse(sp.getReleaseNotesURL());
                }
            });
        }
    }
}

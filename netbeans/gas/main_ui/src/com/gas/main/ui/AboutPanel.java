/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.appservice.api.IAppService;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Unicodes;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXHyperlink;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class AboutPanel extends JPanel {

    final static String T = "<html><center><font size=-2>This software simulates TOPO%s and Gateway%s cloning.</font></center><center><font size=-2>TOPO%s and Gateway%s are registered trademarks of Life Technologies</font></center></html>";
    JXHyperlink siteLink;
    final FontMetrics fm = FontUtil.getDefaultFontMetrics();

    AboutPanel() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        ImageIcon icon = ImageHelper.createImageIcon(ImageNames.ABOUT_LOGO);
        JLabel image = new JLabel(icon);
        add(image, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(fm.getHeight(), 0, fm.getHeight(), 0);
        JComponent title = createTitle();
        add(title, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        JPanel contentPanel = createContentPanel();
        add(contentPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(fm.getHeight() * 2, 0, 0, 0);
        JPanel footer = createFooter();
        add(footer, c);

        hookupListeners();
    }

    private JComponent createTitle() {
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        JLabel label = new JLabel(String.format("<html><b>%s %s</b></html>", "VectorFriends", appService.getCurrentVersion()));
        return label;
    }

    private JPanel createContentPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setOpaque(false);
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        siteLink = new JXHyperlink();
        siteLink.setFocusable(false);
        siteLink.setText("www.VectorFriends.com");
        ret.add(siteLink, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        JLabel label = new JLabel(String.format("Authors:%s", "BioFriends"));
        ret.add(label, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        label = new JLabel(String.format("Computer id:%s", DiskUtil.getComputerFingerprint()));
        ret.add(label, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        label = new JLabel(String.format("License:%s", getLicense()));
        ret.add(label, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        label = new JLabel(String.format("%s2013 BioFriends(Pte Ltd) All rights reserved", Unicodes.COPYRIGHT));
        ret.add(label, c);
        return ret;
    }

    private String getLicense() {
        String ret = "none";
        String secret = LicenseService.getInstance().getSecretCode();
        if (secret != null) {
            boolean isTrial = TrialLicense.isTrialLicense(secret);
            if (isTrial) {
                ret = "trial";
                return ret;
            }
            boolean isFree = FreeAcademicLicense.isFreeAcademicLicense(secret);
            if(isFree){
                ret = "free academic";
                return ret;
            }
            boolean isStatic = ActivationCode.isActivationCode(secret);
            if(isStatic){
                ret = "static";
                return ret;
            }
        }
        return ret;
    }

    private void hookupListeners() {
        siteLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse("www.vectorfriends.com");
            }
        });
    }

    private JPanel createFooter() {
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setOpaque(false);
        UIUtil.setDefaultBorder(ret);
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        JLabel label = new JLabel(String.format(T, Unicodes.TRADEMARK, Unicodes.TRADEMARK, Unicodes.TRADEMARK, Unicodes.TRADEMARK));
        ret.add(label, c);

        return ret;
    }
}

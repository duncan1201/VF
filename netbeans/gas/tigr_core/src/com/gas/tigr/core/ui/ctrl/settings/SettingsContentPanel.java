/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.settings;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.tigr.TIGRSettings;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author dq
 */
public class SettingsContentPanel extends JPanel {

    TIGRSettings tigrSettings;
    
    OverlapPanel overlapPanel;
    MiscPanel miscPanel;

    public SettingsContentPanel() {
        initComponents();
        hookupListeners();
    }
    
    private void initComponents(){
        setLayout(new BorderLayout());
                
        JPanel contentPanel = createContentPanel();   
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void hookupListeners(){
        addPropertyChangeListener(new SettingsContentPanelListeners.PtyListener());
    }
    
    private JPanel createContentPanel(){
        JPanel ret = new JPanel(new GridBagLayout());        
        GridBagConstraints c;        
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        overlapPanel = new OverlapPanel();
        ret.add(overlapPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        miscPanel = new MiscPanel();
        ret.add(miscPanel, c);     
        
        c = new GridBagConstraints();
        JPanel citationPanel = createCitationPanel();
        c.gridx = 0;
        ret.add(citationPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        Component filler = Box.createRigidArea(new Dimension(1,1));
        ret.add(filler, c);
        return ret;
    }
    
    private JPanel createCitationPanel(){
        JPanel ret = new JPanel();
        JXHyperlink link = new JXHyperlink();        
        link.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse("http://ligr-assembler.sourceforge.net");
            }
        });
        link.setText("LIGR Assembler");
        link.setFocusable(false);
        ret.add(link);
        
        ret.add(new JLabel(" is derived from "));
        
        link = new JXHyperlink();        
        link.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse("http://dx.doi.org/10.1089/gst.1995.1.9");
            }
        });
        link.setText("TIGR");
        link.setFocusable(false);
        ret.add(link);
        return ret;
    }

    public void setTigrSettings(TIGRSettings tigrSettings) {
        TIGRSettings old = this.tigrSettings;
        this.tigrSettings = tigrSettings;
        firePropertyChange("tigrSettings", old, this.tigrSettings);
    }
    
    public TIGRSettings getSettingsFromUI(){
        overlapPanel.updateSettingsFromUI(tigrSettings);
        miscPanel.updateSettingsFromUI(tigrSettings);
        return tigrSettings;
    }
}

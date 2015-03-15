/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.proxysetting.impl;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ConnProblemPanel extends JPanel {
    public ConnProblemPanel(Boolean displayProxyButton){
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 3));
        GridBagConstraints c = new GridBagConstraints();
        add(new JLabel("No network connection available! Please check your connection settings."), c);
        
        if(displayProxyButton){
            c = new GridBagConstraints();
            c.gridx = 0;
            c.anchor = GridBagConstraints.LINE_START;
            JButton proxyBtn = new JButton("Proxy Config...");            
            add(proxyBtn, c);
            proxyBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
                    internetSvc.displayProxyConfigDialog();
                }
            });
        }
    }
}

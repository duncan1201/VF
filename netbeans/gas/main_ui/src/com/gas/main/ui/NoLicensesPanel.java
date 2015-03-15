/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.util.UIUtil;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author dq
 */
class NoLicensesPanel extends JPanel{
    
    static String title = "License";
    
    JRadioButton requestTrialBtn;
    JRadioButton importLicenseBtn;
    JRadioButton basicModeBtn;
    
    NoLicensesPanel(){
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        add(new JLabel("<html>No licenses found, please select one of the following options:</br></html>"), c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        requestTrialBtn = new JRadioButton("Request a trial license", true);
        add(requestTrialBtn, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        importLicenseBtn = new JRadioButton("Import an existing license");
        add(importLicenseBtn, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        basicModeBtn = new JRadioButton("Start in basic mode");
        add(basicModeBtn, c);        
        
        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(requestTrialBtn);
        bGroup.add(importLicenseBtn);
        bGroup.add(basicModeBtn);
    }
    
    public boolean isRequestTrialLicense(){
        return requestTrialBtn.isSelected();
    }
    
    public boolean isImportLicense(){
        return importLicenseBtn.isSelected();
    }
    
    public boolean isBasicMode(){
        return basicModeBtn.isSelected();
    }
}

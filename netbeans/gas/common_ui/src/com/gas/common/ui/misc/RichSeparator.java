/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author dq
 */
public class RichSeparator extends JPanel {

    public RichSeparator(JComponent comp) {
        setLayout(new BorderLayout());
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        centerPanel.add(comp, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        centerPanel.add(new JSeparator(), c);
        
        add(centerPanel, BorderLayout.CENTER);       
    }

    public RichSeparator(String title) {
        this(null, title);
    }
    
    public void setLeftDecoration(JComponent comp){
        add(comp, BorderLayout.WEST);
    }
    
    public void setRightDecoratioin(JComponent comp){
        add(comp, BorderLayout.EAST);
    }

    public RichSeparator(Icon icon, String title) {
        setLayout(new BorderLayout());
               
        JPanel centerPanel = new JPanel(new GridBagLayout());        
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        JLabel label = new JLabel();
        if (icon != null) {
            label.setIcon(icon);
        }
        label.setText(title);        
        centerPanel.add(label, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        centerPanel.add(new JSeparator(), c);
        add(centerPanel, BorderLayout.CENTER);
    }
}

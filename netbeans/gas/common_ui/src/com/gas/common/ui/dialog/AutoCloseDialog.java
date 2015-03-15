/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.dialog;

import com.gas.common.ui.util.ColorCnst;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author dq
 */
class AutoCloseDialog extends JDialog {
    JLabel label;
    String msg;
    AutoCloseDialog(Frame owner, String msg){
        super(owner, false);
        this.msg = msg;
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        getContentPane().setLayout(new BorderLayout());
        
        label = new JLabel(msg);
        label.setOpaque(true);
        label.setBackground(ColorCnst.TOOLTIP_BG);
        
        GridBagConstraints c = new GridBagConstraints();
        label.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        getContentPane().add(label, BorderLayout.CENTER);
        setBackground(ColorCnst.TOOLTIP_BG);
    }
}

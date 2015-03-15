/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.progress;

import com.gas.common.ui.util.UIUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class ProgressBarPanel extends JPanel {

    JLabel msgMain;
    JProgressBar progressBar;

    public ProgressBarPanel() {
        final Insets insets = UIUtil.getDefaultInsets();
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.insets = new Insets(insets.top, insets.left, 0, insets.right);
        msgMain = new JLabel("Running...");
        //msgMain.setHorizontalTextPosition(SwingConstants.CENTER);
        msgMain.setHorizontalAlignment(SwingConstants.CENTER);
        add(msgMain, c);

        c = new GridBagConstraints();
        c.insets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        progressBar = new JProgressBar(0, 100);
        add(progressBar, c);
    }

    void setPrototypeMessage(String msg) {
        final Insets insets = UIUtil.getDefaultInsets();
        final FontMetrics fm = this.getFontMetrics(msgMain.getFont());
        int strWidth = fm.stringWidth(msg);
        UIUtil.setPreferredWidth(msgMain, strWidth + insets.left + insets.right);
    }

    void setValue(int value) {
        progressBar.setValue(value);
    }

    void setIndeterminate(boolean i) {
        progressBar.setIndeterminate(i);
    }
}

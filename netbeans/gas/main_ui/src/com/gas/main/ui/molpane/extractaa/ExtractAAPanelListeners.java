/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.extractaa;

import com.gas.common.ui.util.UIUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

/**
 *
 * @author dq
 */
public class ExtractAAPanelListeners {
    
    static class TargetsListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton src = (JRadioButton)e.getSource();
            ExtractAAPanel p = UIUtil.getParent(src, ExtractAAPanel.class);
            String cmd = src.getActionCommand();
            p.relativeStart.setEnabled(cmd.equalsIgnoreCase("selected"));
        }
    
    }
    
    static class FramesListeners implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComponent src = (JComponent)e.getSource();
            ExtractAAPanel p = UIUtil.getParent(src, ExtractAAPanel.class);
            p.validateInput();
        }
    }
}

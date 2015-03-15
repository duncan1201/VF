/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.exportAsImage;

import com.gas.common.ui.util.UIUtil;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class CtrlPanelListeners {
    
    static class VisibleOnlyListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox)e.getSource();
           
            ExportImagePanel exportImagePanel = UIUtil.getParent(src, ExportImagePanel.class);
            BufferedImage image = exportImagePanel.getPrintEditor().createImageForExporting(src.isSelected(), Transparency.TRANSLUCENT);
            exportImagePanel.getPreviewPanel().getPreviewPage().setImage(image);
        }
    }
    
    static class ZoomListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner)e.getSource();
            ExportImagePanel exportImagePanel = UIUtil.getParent(src, ExportImagePanel.class);
            Double value = (Double)src.getValue();
            exportImagePanel.getPreviewPanel().getPreviewPage().setScale(value.floatValue());
        }
    }
    
    static class FormatListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
}

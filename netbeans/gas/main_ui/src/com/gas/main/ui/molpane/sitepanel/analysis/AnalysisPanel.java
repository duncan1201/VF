/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.analysis;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.gc.api.GCResult;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.jdesktop.swingx.VerticalLayout;

/**

 @author dq
 */
public class AnalysisPanel extends JPanel {
    
    GCPlotPanel gcPlotPanel;
    
    public AnalysisPanel(){
        setToolTipText("Analysis");
        setLayout(new BorderLayout());
        TitledPanel titledPanel = new TitledPanel("Analysis");
        add(titledPanel, BorderLayout.CENTER);
        
        LayoutManager layout = new VerticalLayout();        
        titledPanel.getContentPane().setLayout(layout);
        
        gcPlotPanel = new GCPlotPanel();
        titledPanel.getContentPane().add(gcPlotPanel);
        
        UIUtil.setBackground(titledPanel.getContentPane(), CNST.BG, JPanel.class);
    }
    
    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.LINE_PLOT_16);
    }
    
    public void setGCResult(GCResult gcResult){
        gcPlotPanel.setGcResult(gcResult);
    }

    public GCPlotPanel getGcPlotPanel() {
        return gcPlotPanel;
    }
    
}

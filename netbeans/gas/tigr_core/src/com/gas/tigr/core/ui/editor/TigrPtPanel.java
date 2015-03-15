/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor;

import com.gas.tigr.core.ui.ctrl.CtrlPanel;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.tigr.core.ui.ctrl.contigs.ContigsPanel;
import com.gas.tigr.core.ui.ctrl.settings.SettingsContentPanel;
import com.gas.tigr.core.ui.ctrl.settings.SettingsPanel;
import com.gas.tigr.core.ui.editor.shared.AssemblyPanel;
import com.gas.tigr.core.ui.editor.shared.MiniMap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSplitPane;

/**
 *
 * @author dq
 */
public class TigrPtPanel extends JPanel {

    protected MiniMap miniMap;
    private AssemblyPanel assemblyPanel;
    private CtrlPanel ctrlPanel;
    protected boolean scrollValueAdjusting ;
    

    public TigrPtPanel() {
        setLayout(new BorderLayout());
        JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        ctrlPanel = getCtrlPanel();
        hSplitPane.setRightComponent(ctrlPanel);
        assemblyPanel = new AssemblyPanel();
        Font baseFont = FontUtil.getDefaultMSFont();
        assemblyPanel.setBaseFont(baseFont);        
        JPanel c = new JPanel(new BorderLayout());
        miniMap = new MiniMap();
        UIUtil.setPreferredHeight(miniMap, 30);
        c.add(miniMap, BorderLayout.NORTH);        
        c.add(assemblyPanel, BorderLayout.CENTER);        
        hSplitPane.setLeftComponent(c);

        hSplitPane.setResizeWeight(1);
        hSplitPane.setDividerLocation(500);

        add(c, BorderLayout.CENTER);
        add(ctrlPanel, BorderLayout.EAST);

        hookupListeners();
    }

    public MiniMap getMiniMap() {
        return miniMap;
    }

    public void setMiniMap(MiniMap miniMap) {
        this.miniMap = miniMap;
    }

    public boolean isScrollValueAdjusting() {
        return scrollValueAdjusting;
    }

    public void setScrollValueAdjusting(boolean scrollValueAdjusting) {
        this.scrollValueAdjusting = scrollValueAdjusting;
    }
    
    private void hookupListeners() {
    }

    public CtrlPanel getCtrlPanel() {
        if (ctrlPanel == null) {
            ctrlPanel = new CtrlPanel();
        }
        return ctrlPanel;
    }

    public AssemblyPanel getAssemblyPanel() {
        return assemblyPanel;
    }

    public SettingsPanel getSettingsPanel(){
        return getCtrlPanel().getSettingsPanel();
    }

    public ContigsPanel getContigsPanel() {
        return getCtrlPanel().getContigsPanel();
    }        
}

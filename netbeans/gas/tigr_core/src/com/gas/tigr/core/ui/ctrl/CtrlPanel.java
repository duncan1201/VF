/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl;

import com.gas.common.ui.tabbedpane.JTabbedPaneFactory;
import com.gas.tigr.core.ui.ctrl.contigs.ContigsPanel;
import com.gas.tigr.core.ui.ctrl.contigs.ContigsTable;
import com.gas.tigr.core.ui.ctrl.settings.SettingsContentPanel;
import com.gas.tigr.core.ui.ctrl.settings.SettingsPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class CtrlPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private ContigsPanel contigsPanel;
    private SettingsPanel settingsPanel;

    public CtrlPanel() {
        LayoutManager layout = null;
        layout = new BorderLayout();
        setLayout(layout);
        tabbedPane = new JTabbedPane(SwingConstants.RIGHT);
        tabbedPane = JTabbedPaneFactory.create(SwingConstants.RIGHT, Color.RED);

        contigsPanel = new ContigsPanel();

        tabbedPane.addTab("", contigsPanel.getImageIcon(), contigsPanel);

        settingsPanel = new SettingsPanel();
        tabbedPane.addTab("", settingsPanel.getImageIcon(), settingsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        hookupListeners();
    }

    public ContigsPanel getContigsPanel() {
        return contigsPanel;
    }  

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }
    
    private void hookupListeners(){
        ContigsTable contigsTbl = contigsPanel.getContigsTable();
        contigsPanel.getContigsTable().getSelectionModel().addListSelectionListener(new CtrlPanelListeners.ContigsTblListener(contigsTbl));
    }
}

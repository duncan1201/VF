/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl;

import com.gas.msa.ui.alignment.ctrl.general.GeneralPanel;
import com.gas.common.ui.tabbedpane.JTabbedPaneFactory;
import com.gas.domain.core.msa.MSA;
import com.gas.msa.ui.alignment.ctrl.param.ParamsPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class CtrlPanel extends JPanel {

    WeakReference<GeneralPanel> generalPanelRef;
    ParamsPanel paramsPanel;
    private JTabbedPane tabbedPane;
    private MSA msa;

    public CtrlPanel() {
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        LayoutManager layout = null;
        layout = new BorderLayout();
        setLayout(layout);

        tabbedPane = JTabbedPaneFactory.create(SwingConstants.RIGHT, Color.LIGHT_GRAY);
        add(tabbedPane, BorderLayout.CENTER);
        
        GeneralPanel generalPanel = new GeneralPanel();
        tabbedPane.addTab("", generalPanel.getImageIcon(), generalPanel);
        generalPanelRef = new WeakReference<GeneralPanel>(generalPanel);          
        
        paramsPanel = new ParamsPanel();
        tabbedPane.addTab("", paramsPanel.getImageIcon(), paramsPanel);
                 
        //IPredictPanelCreator predictPanelCreator = Lookup.getDefault().lookup(IPredictPanelCreator.class);
        //IPredictPanel predictPanel = predictPanelCreator.create();
        //tabbedPaneRef.get().addTab("", predictPanel.getImageIcon(), (JPanel)predictPanel);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new CtrlPanelListeners.PtyListener());
    }

    public void setMsa(MSA msa) {
        MSA old = this.msa;
        this.msa = msa;
        firePropertyChange("msa", old, this.msa);
    }

    public GeneralPanel getGeneralPanel() {
        return generalPanelRef.get();
    }
}

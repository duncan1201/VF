/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.phylo.clustalw;

import com.gas.domain.core.msa.MSA;
import com.gas.phylo.mrbayers.api.IMrBayesPanel;
import com.gas.phylo.mrbayers.api.IMrBayesPanelService;
import java.awt.BorderLayout;
import java.awt.Component;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class TreePanel extends JPanel {
    
    MSA msa;
    WeakReference<ClustalwPanel> clustalwPanelRef;
    IMrBayesPanel mrBayersPanel;
    JTabbedPane pane;
    
    public TreePanel(){
        initComponents();
        hookupListeners();
    }
    
    public Component getSelectedComponent(){
        return pane.getSelectedComponent();
    }
    
    private void initComponents(){
        setLayout(new BorderLayout());
        pane = new JTabbedPane();
        ClustalwPanel clustalwPanel = new ClustalwPanel();
        clustalwPanelRef = new WeakReference<ClustalwPanel>(clustalwPanel);
        pane.add("Clustal", clustalwPanel);
        
        IMrBayesPanelService c = Lookup.getDefault().lookup(IMrBayesPanelService.class);
        if(c != null){
            mrBayersPanel = c.create();
            pane.add("MrBayers", (JComponent)mrBayersPanel);
        }
        
        add(pane, BorderLayout.CENTER);        
    }
    
    private void hookupListeners(){
        addPropertyChangeListener(new TreePanelListeners.PtyListener());
    }

    public void setMsa(MSA msa) {
        MSA old = this.msa;
        this.msa = msa;
        firePropertyChange("msa", old, this.msa);
    }
    
    public MSA getMsa(){
        return msa;
    }
    
}

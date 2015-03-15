/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.main.ui.molpane.sitepanel.primer3.out.OutTree;
import com.gas.domain.core.primer3.Oligo;
import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
class OutMoreOligosPanel extends JPanel {
    
    OutTree outTree;
    Set<Oligo> oligos = new HashSet<Oligo>();
    
    OutMoreOligosPanel(){
        setLayout(new BorderLayout());
        
        outTree = new OutTree();
        outTree.setRootVisible(false);
        JScrollPane scrollPane = new JScrollPane(outTree);
        add(scrollPane, BorderLayout.CENTER); 
        
        hookupListeners();
    }
    
    private void hookupListeners(){
        addPropertyChangeListener(new OutMoreOligosPanelListeners.PtyListener());
    }
    
    void setOligos(Set<Oligo> oligos){
        Set<Oligo> old = this.oligos;
        this.oligos = oligos;
        firePropertyChange("oligos", old, this.oligos);
    }
}

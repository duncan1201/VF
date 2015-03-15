/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.ancestor;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.editor.as.lineage.AncestorsComp;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
public class AncestorPanel extends JPanel {
    
    AncestorsComp ancestorComp;
    
    public AncestorPanel(){
        setLayout(new BorderLayout());        
        ancestorComp = new AncestorsComp();
        ancestorComp.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(ancestorComp);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public AncestorsComp getAncestorsComp(){
        return ancestorComp;
    }
    
    public void populateUI(AnnotatedSeq as){
        ancestorComp.setAs(as);
    }
}

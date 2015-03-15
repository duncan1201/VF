/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import javax.swing.Box;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class SidePanel extends JPanel {

    private GeneralPanel generalPanel;
    private WeakReference<NodePanel> nodePanelRef;
    private WeakReference<BranchPanel> branchPanelRef;
    
    SidePanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        CollapsibleTitlePanel titlePaneGeneral = new CollapsibleTitlePanel("General");
        generalPanel = new GeneralPanel();
        titlePaneGeneral.setContentPane(generalPanel);
        add(titlePaneGeneral, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        CollapsibleTitlePanel titlePaneNode = new CollapsibleTitlePanel("Node");
        NodePanel nodePanel = new NodePanel();
        titlePaneNode.setContentPane(nodePanel);
        add(titlePaneNode, c);
        nodePanelRef = new WeakReference<NodePanel>(nodePanel);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        CollapsibleTitlePanel titlePaneBranch = new CollapsibleTitlePanel("Branch");
        BranchPanel branchPanel = new BranchPanel();
        branchPanelRef = new WeakReference<BranchPanel>(branchPanel);
        titlePaneBranch.setContentPane(branchPanel);
        add(titlePaneBranch, c);
        
        Dimension widest = UIUtil.widest(titlePaneGeneral, titlePaneNode, titlePaneBranch);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        
        Component comp = Box.createRigidArea(new Dimension(widest.width + 10,1));
        add(comp, c);

    }
    
    public void populateUI(MSA msa){
        getGeneralPanel().initValues(msa);
        getBranchPanel().initValues(msa);
        getNodePanel().initValues(msa);
    }
    
    public GeneralPanel getGeneralPanel(){
        return generalPanel;
    }
    
    public NodePanel getNodePanel(){
        return nodePanelRef.get();
    }
    
    public BranchPanel getBranchPanel(){
        return branchPanelRef.get();
    }
}

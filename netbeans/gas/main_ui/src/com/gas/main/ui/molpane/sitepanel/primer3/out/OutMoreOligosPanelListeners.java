/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.main.ui.molpane.sitepanel.primer3.out.OutMoreOligosPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.out.OligoTreeNode;
import com.gas.common.ui.util.UIUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author dq
 */
class OutMoreOligosPanelListeners {
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            OutMoreOligosPanel src = (OutMoreOligosPanel)evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(name.equalsIgnoreCase("oligos")){
                if(src.oligos != null){
                    OligoTreeNode node = new OligoTreeNode(v);
                    src.outTree.setModel(new DefaultTreeModel(node));
                    UIUtil.expandTree(src.outTree, 2);
                }else{                    
                    src.outTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));                    
                }
            }
        }
    }
}

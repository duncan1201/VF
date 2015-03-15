/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.main.ui.molpane.sitepanel.primer3.out.OligoTreeNode;
import com.gas.main.ui.molpane.sitepanel.primer3.out.OutTree;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.Oligo;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author dq
 */
class OutOligoPanel extends JPanel {

    Oligo oligo;
    OutTree tree;

    OutOligoPanel() {
        setLayout(new BorderLayout());
        tree = new OutTree();

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);

        hookupListeners();
    }

    void refresh() {
        if (oligo != null) {
            OligoTreeNode node = new OligoTreeNode(oligo);
            tree.setModel(new DefaultTreeModel(node));
            UIUtil.expandTree(tree, Integer.MAX_VALUE);
        } else {
            tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        }
    }

    private void hookupListeners() {
        addPropertyChangeListener(new OutOligoPanelListeners.PtyListener());
    }

    void setOligo(Oligo oligo) {
        Oligo old = this.oligo;
        this.oligo = oligo;
        firePropertyChange("oligo", old, this.oligo);
    }
}

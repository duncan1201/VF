/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.main.ui.molpane.action.Convert2OligoAction;
import com.gas.main.ui.molpane.action.PCRAction;
import java.awt.Dimension;
import java.lang.ref.WeakReference;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author dq
 */
public class OutTree extends JTree {

    WeakReference<JPopupMenu> popupMenuRef;

    public OutTree() {
        setCellRenderer(new OutTreeRenderer());
        setEditable(true);
        setCellEditor(new OutTreeEditor(this));
        setRootVisible(true);
        ToolTipManager.sharedInstance().registerComponent(this);

        hookupListeners();
    }

    JPopupMenu getPopupMenu() {
        if (popupMenuRef == null || popupMenuRef.get() == null) {
            JPopupMenu p = createPopupMenu();
            popupMenuRef = new WeakReference<JPopupMenu>(p);
        }
        return popupMenuRef.get();
    }

    private void hookupListeners() {
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        getSelectionModel().addTreeSelectionListener(new OutTreeListeners.SelectionListener(this));

        addMouseListener(new OutTreeListeners.MouseAdpt());
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        //popupMenu.add(new OutTreeListeners.EditPrimerListener(this));
        popupMenu.add(new PCRAction(this));
        popupMenu.add(new Convert2OligoAction(this));
        return popupMenu;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
}

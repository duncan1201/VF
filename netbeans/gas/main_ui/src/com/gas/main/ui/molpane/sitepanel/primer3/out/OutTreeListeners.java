/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.action.Convert2OligoAction;
import com.gas.main.ui.molpane.action.PCRAction;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author dq
 */
class OutTreeListeners {

    static class MouseAdpt extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent e) {
            OutTree src = (OutTree) e.getSource();
            Point pt = e.getPoint();
            if (e.isPopupTrigger()) {
                src.getRowForLocation(pt.x, pt.y);
                TreePath treePath = src.getLeadSelectionPath();
                if (treePath == null) {
                    return;
                }
                OligoTreeNode node = (OligoTreeNode) treePath.getLastPathComponent();
                boolean isOligoElement = node.isOligoElement();
                boolean isOligo = node.isOligo();
                Integer no = null;
                Oligo oligo = null;
                OligoElement oe = null;
                if (isOligo) {
                    oligo = (Oligo) node.getUserObject();
                    no = oligo.getNo();
                } else if (isOligoElement) {
                    oe = (OligoElement) node.getUserObject();
                    no = oe.getNo();
                    OligoTreeNode nodeOligo = node.getAncestor(Oligo.class);
                    oligo = (Oligo)nodeOligo.getUserObject();
                }
                JPopupMenu p = src.getPopupMenu();
                List<JMenuItem> menuItems = UIUtil.getMenuItems(p);
                for (JMenuItem item : menuItems) {
                    String text = item.getText();
                    if (text.equals(PCRAction.TEXT)) {
                        PCRAction pcrAction = (PCRAction) item.getAction();
                        item.setEnabled(oligo.getLeft() != null && oligo.getRight() != null);
                        if (no != null) {
                            pcrAction.setNo(no);
                        }
                    } else if (text.equals(Convert2OligoAction.TEXT)) {
                        item.setEnabled(isOligoElement);
                        if (isOligoElement) {
                            Convert2OligoAction convert = (Convert2OligoAction) item.getAction();
                            if (no != null && oe != null) {
                                convert.setNo(no);
                                convert.setWhich(oe.getName());
                            }
                        }
                    }
                }
                p.show(src, pt.x, pt.y);
                System.out.print("");
            }
        }
    }

    static class SelectionListener implements TreeSelectionListener {

        WeakReference<OutTree> outTreeRef;

        SelectionListener(OutTree outTree) {
            outTreeRef = new WeakReference<OutTree>(outTree);
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath path = e.getNewLeadSelectionPath();
            if (path == null) {
                return;
            }
            OligoTreeNode node = (OligoTreeNode) path.getLastPathComponent();
            if (node.isOligo()) {
                return;
            }
            Oligo oligo = null;
            OligoElement oe = null;
            if (node.isOligoElement()) {
                oe = (OligoElement) node.getUserObject();
                OligoTreeNode nodeOligo = (OligoTreeNode) node.getParent();
                oligo = (Oligo) nodeOligo.getUserObject();
            } else if (node.isFloat() || node.isString()) {
                OligoTreeNode nodeOe = (OligoTreeNode) node.getParent();
                oe = (OligoElement) nodeOe.getUserObject();
                OligoTreeNode nodeOligo = (OligoTreeNode) nodeOe.getParent();
                oligo = (Oligo) nodeOligo.getUserObject();
            }
            if (oe == null) {
                return;
            }
            String label = OutTreeRenderer.createTreeText(oe, oligo, false);
            Loc loc = new Loc(oe.calculateStart(), oe.calculateEnd());
            loc.setStrand(true);
            MolPane molPane = UIUtil.getParent(outTreeRef.get(), MolPane.class);
            List list = molPane.getShapeData(FetureKeyCnst.PRIMER_BINDING_SITE, label);
            if (!list.isEmpty()) {
                Feture feture = (Feture) list.get(0);
                molPane.setSelectedFeture(feture);
            } else {
                molPane.setSelection(loc);
            }
            molPane.center(loc);
        }
    }
}

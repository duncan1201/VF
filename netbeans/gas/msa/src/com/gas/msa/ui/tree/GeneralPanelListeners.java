/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.pref.MSAPref;
import com.gas.msa.ui.MSAEditor;
import com.gas.msa.ui.common.ITree;
import com.gas.msa.ui.common.TreeNode;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class GeneralPanelListeners {

    static class ColorBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            TreeNode.Node selected = treePane.getSelected();
            if (selected == null) {
                String title = "Cannot perform coloring";
                String msg = String.format(CNST.MSG_FORMAT, "Cannot perform coloring", "Please select a node in the tree");
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(title);
                DialogDisplayer.getDefault().notify(m);
                return;
            }
            Color color = JColorChooser.showDialog(WindowManager.getDefault().getMainWindow(), "Color Nodes", Color.BLACK);
            if (color != null) {
                selected.setColor(color, true);
                MSAEditor msaEditor = UIUtil.getParent(src, MSAEditor.class);
                MSA msa = msaEditor.getMsa();
                msa.updateNewickStr();
                treePane.paintChildren();
            }
        }
    }

    static class FontSizeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner) e.getSource();
            Integer value = (Integer) src.getValue();

            MSAPref.getInstance().setFontSize(value.floatValue());
        }
    }

    static class TreeFormBtnListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JToggleButton src = (JToggleButton) e.getSource();
            String cmd = src.getActionCommand();

            TreePane.FORM form = TreePane.FORM.get(cmd);

            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            MSAEditor editor = UIUtil.getParent(treePane, MSAEditor.class);
            if (treePane != null && e.getStateChange() == ItemEvent.SELECTED) {
                treePane.setForm(form);
                editor.getMsa().getMsaSetting().setTreeForm(form.toString());
            }
        }
    }

    static class SameLengthListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox) e.getSource();
            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            MSAEditor msaEditor = UIUtil.getParent(treePane, MSAEditor.class);
            int state = e.getStateChange();

            if (state == ItemEvent.SELECTED) {
                treePane.setTransform(ITree.TRANSFORM.EQUAL);
                msaEditor.getMsa().getMsaSetting().setTreeEdgeTransform(ITree.TRANSFORM.EQUAL.toString());
            } else if (state == ItemEvent.DESELECTED) {
                treePane.setTransform(ITree.TRANSFORM.CLADOGRAM);
                msaEditor.getMsa().getMsaSetting().setTreeEdgeTransform(ITree.TRANSFORM.CLADOGRAM.toString());
            }
        }
    }

    static class EdgeFormListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton src = (JRadioButton) e.getSource();
            TreePane treePane = UIUtil.getParent(src, TreePane.class);
            MSAEditor editor = UIUtil.getParent(src, MSAEditor.class);
            String cmd = src.getActionCommand();
            GeneralPanel generalPanel = UIUtil.getParent(src, GeneralPanel.class);

            if (cmd.equalsIgnoreCase("cladogram")) {
                generalPanel.getSameLengthBtn().setEnabled(true);
                boolean sameLength = generalPanel.getSameLengthBtn().isSelected();
                if (sameLength) {
                    treePane.setTransform(ITree.TRANSFORM.EQUAL);
                    editor.getMsa().getMsaSetting().setTreeEdgeTransform(ITree.TRANSFORM.EQUAL.toString());
                } else {
                    treePane.setTransform(ITree.TRANSFORM.CLADOGRAM);
                    editor.getMsa().getMsaSetting().setTreeEdgeTransform(ITree.TRANSFORM.CLADOGRAM.toString());
                }
            } else if (cmd.equalsIgnoreCase("phylogram")) {
                generalPanel.getSameLengthBtn().setEnabled(false);
                treePane.setTransform(ITree.TRANSFORM.NONE);
                editor.getMsa().getMsaSetting().setTreeEdgeTransform(ITree.TRANSFORM.NONE.toString());
            }            
        }
    }
    
}

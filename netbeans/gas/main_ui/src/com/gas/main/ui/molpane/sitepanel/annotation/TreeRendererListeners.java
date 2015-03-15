/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AsPref;
import com.gas.domain.core.as.Feture;
import com.gas.domain.ui.pref.ColorPref;
import com.gas.main.ui.editor.as.ASEditor;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class TreeRendererListeners {

    static class VisibleListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            Component src = (Component) e.getSource();
            TreeRenderer.Summary summary = UIUtil.getParent(src, TreeRenderer.Summary.class);
            ASEditor editor = UIUtil.getParent(src, ASEditor.class);
            if (editor == null) {
                return;
            }
            AsPref trackPref = editor.getAnnotatedSeq().getAsPref();
            String key = summary.getFeatureKey().toUpperCase(Locale.ENGLISH);
            final int state = e.getStateChange();
            trackPref.setTrackVisible(key, state == ItemEvent.SELECTED);
        }
    }

    static class ColorBtnListener implements ActionListener {

        String msg = "Annotation Color for Track %s";

        ColorBtnListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final JComponent src = (JComponent) e.getSource();
            TreeRenderer.Summary summary = UIUtil.getParent(src, TreeRenderer.Summary.class);
            final String featureKey = summary.getFeatureKey();
            Color initialColor = ColorPref.getInstance().getColor(featureKey);
            final JColorChooser chooser = new JColorChooser(initialColor);
            JDialog dialog = JColorChooser.createDialog(WindowManager.getDefault().getMainWindow(), String.format(msg, featureKey), true, chooser, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    Color ret = chooser.getColor();
                    ColorPref.getInstance().putColor(featureKey, ret);
                    src.revalidate();
                    final JTree tree = UIUtil.getParent(src, JTree.class);
                    tree.revalidate();
                    TreePath treePath = tree.getSelectionPath();
                    FeatureTreeNode c = (FeatureTreeNode) treePath.getLastPathComponent();
                    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                    model.nodeChanged(c);

                    UIUtil.simulateMousePress(tree.getLocationOnScreen());                    
                }
            }, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                } // empty
            });
            dialog.setVisible(true);
        }
    }
}

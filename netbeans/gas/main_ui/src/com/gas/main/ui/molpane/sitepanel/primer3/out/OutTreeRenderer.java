/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author dq
 */
class OutTreeRenderer implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component ret = null;
        if (!(value instanceof OligoTreeNode)) {
            return new JLabel("");
        }
        final NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        final NumberFormat nfPc = NumberFormat.getPercentInstance(Locale.ENGLISH);
        nfPc.setMinimumFractionDigits(1);
        nfPc.setMaximumFractionDigits(1);

        OligoTreeNode node = (OligoTreeNode) value;
        Object userObject = node.getUserObject();
        if (node.isOligo()) {

            Oligo oligo = (Oligo) userObject;
            OligoPanel oligoPanel = new OligoPanel(oligo);
            if (oligo.getProductSize() != null) {
                oligoPanel.setText(String.format("Oligo %d (Product Size:%d)", oligo.getNo() + 1, oligo.calculateProductSize()));
            } else {
                oligoPanel.setText(String.format("Oligo %d", oligo.getNo() + 1));
            }
            //oligoPanel.setSelected(selected);

            ret = oligoPanel;
        } else if (node.isOligoElement()) {
            JLabel label = new JLabel();
            OligoElement oe = (OligoElement) userObject;
            OligoTreeNode nodeParent = (OligoTreeNode) node.getParent();
            Oligo oligo = (Oligo) nodeParent.getUserObject();

            String text = createTreeText(oe, oligo, true);
            if (text != null) {
                label.setText(text);
            }
            //ret = label;
            OEPanel oePanel = new OEPanel();
            oePanel.setOligo(oligo);
            oePanel.setOligoElement(oe);
            ret = oePanel;
        } else if (node.isCollection()) {
            return new JLabel();
        } else {
            throw new UnsupportedOperationException(String.format("Unrecognized node: %s", node.getUserObject().getClass().toString()));
        }

        return ret;
    }

    static String createTreeText(OligoElement oe, Oligo oligo, boolean loc) {
        int start = oe.calculateStart();
        int end = oe.calculateEnd();
        String text = null;
        if (oe.getName().equalsIgnoreCase("Left")) {
            if (loc) {
                text = String.format("Forward Primer %d(%d-%d)", oligo.getNo() + 1, start, end);
            } else {
                text = String.format("Forward Primer %d", oligo.getNo() + 1);
            }
        } else if (oe.getName().equalsIgnoreCase("Internal")) {
            if (loc) {
                text = String.format("DNA Probe %d(%d-%d)", oligo.getNo() + 1, start, end);
            } else {
                text = String.format("DNA Probe %d", oligo.getNo() + 1);
            }
        } else if (oe.getName().equalsIgnoreCase("Right")) {
            if (loc) {
                text = String.format("Reverse Primer %d(%d-%d)", oligo.getNo() + 1, start, end);
            } else {
                text = String.format("Reverse Primer %d", oligo.getNo() + 1);
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return text;
    }   

    static class OligoPanel extends JPanel {

        WeakReference<JLabel> labelRef;
        Oligo oligo;

        OligoPanel(Oligo oligo) {
            this.oligo = oligo;
            setOpaque(false);
            setLayout(new GridBagLayout());
            GridBagConstraints c;

            c = new GridBagConstraints();
            JLabel label = new JLabel();
            label.setOpaque(false);
            labelRef = new WeakReference<JLabel>(label);
            add(label, c);

            c = new GridBagConstraints();
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            Component comp = Box.createRigidArea(new Dimension(16, 16));
            add(comp, c);

            hookupListeners();
        }
        
        private void hookupListeners() {
        }

        void setText(String t) {
            labelRef.get().setText(t);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.api.IExternalURIService;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Qualifier;
import com.gas.domain.core.as.AsPref;
import com.gas.domain.ui.pref.ColorPref;
import com.gas.domain.ui.shape.ArrowImageCreator;
import com.gas.main.ui.editor.as.ASEditor;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import org.jdesktop.swingx.JXHyperlink;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class TreeRenderer implements TreeCellRenderer {

    private IExternalURIService service = Lookup.getDefault().lookup(IExternalURIService.class);
    private AsPref trackPref ;
    private Summary summary;
    private JLabel label;
    private LinkPanel linkPanel;


    TreeRenderer() {
        summary = new Summary();
        label = new JLabel();
        linkPanel = new LinkPanel();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (!(value instanceof FeatureTreeNode)) {
            label.setText("");
            return label;
        }
        if(trackPref == null){
            ASEditor editor = UIUtil.getParent(tree, ASEditor.class);
            trackPref = editor.getAnnotatedSeq().getAsPref();
        }
        Component ret = null;

        FeatureTreeNode node = (FeatureTreeNode) value;
        Object userObject = node.getUserObject();
        if (userObject instanceof List) {
            List<Feture> fetures = (List<Feture>) userObject;
            String key = fetures.get(0).getKey();
            summary.setText(String.format("%s(%d)", key, fetures.size()));
            if (trackPref.isTrackVisible(key.toUpperCase(Locale.ENGLISH))) {
                summary.setChecked(true);
            } else {
                summary.setChecked(false);
            }
            ret = summary;
        } else if (userObject instanceof Feture) {
            Feture feture = (Feture) userObject;
            Lucation luc = feture.getLucation();
            label.setText(String.format("%s:%s", feture.getKey(), luc.toString()));
            ret = label;
        } else if (userObject instanceof Qualifier) {
            final Qualifier q = (Qualifier) userObject;
            if (q.isNote()) {
                label.setText(StrUtil.toMultiLine(q.getValue(), 39));
                ret = label;
            } else if (q.isProteinId()) {
                linkPanel.setKey(q.getKey());
                linkPanel.setValue(q.getValue());
                ret = linkPanel;
            } else if (q.isDbXref()) {
                final String v = q.getValue();
                final String db = v.substring(0, v.indexOf(':'));
                boolean supported = service.isBrowsingSupported(db);
                if (supported) {
                    linkPanel.setKey(q.getKey());
                    linkPanel.setValue(q.getValue());
                    ret = linkPanel;
                } else {
                    label.setText(StrUtil.toMultiLine(q.getKey(), q.getValue(), 39));
                    ret = label;
                }
            } else {
                label.setText(StrUtil.toMultiLine(q.getKey(), q.getValue(), 39));
                ret = label;
            }
        } else {
            label.setText("");
            ret = label;
        }

        return ret;
    }

    
    static class Summary extends JPanel {

        private JCheckBox checkbox;
        private JLabel label;
        private JButton colorBtn;
        private static Integer imageWidth;
        private static Integer imageHeight;

        Summary() {
            super(new GridBagLayout());
            setOpaque(false);
            GridBagConstraints c = null;

            c = new GridBagConstraints();
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            colorBtn = new FlatBtn(false);
            colorBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            colorBtn.setPreferredSize(new Dimension(getImageWidth(), getImageHeight()));
            add(colorBtn, c);
            
            c = new GridBagConstraints();
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            checkbox = getCheckbox();
            add(checkbox, c);            

            label = getLabel();
            c = new GridBagConstraints();
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            add(label, c);

            hookupListeners();
        }

        private void hookupListeners() {
            checkbox.addItemListener(new TreeRendererListeners.VisibleListener());
            colorBtn.addActionListener(new TreeRendererListeners.ColorBtnListener());
        }

        void setText(String text) {
            getLabel().setText(text);
            final String featureKey = getFeatureKey();
            Image image = ArrowImageCreator.createImage(ColorPref.getInstance().getColor(featureKey), getImageWidth(), getImageHeight());
            Icon icon = new ImageIcon(image, "");
            colorBtn.setIcon(icon);
            Dimension cSize = colorBtn.getPreferredSize();
            Dimension lSize = getLabel().getPreferredSize();
            Dimension cbSize = getCheckbox().getPreferredSize();
            UIUtil.setPreferredWidth(this, cSize.width + lSize.width + cbSize.width);
        }

        private JLabel getLabel() {
            if (label == null) {
                label = new JLabel();
            }
            return label;
        }

        static Integer getImageHeight() {
            if (imageHeight == null) {
                Font font = FontUtil.getDefaultSansSerifFont();
                imageHeight = Math.round(FontUtil.getFontMetrics(font).getHeight() * 0.60f);
            }
            return imageHeight;
        }

        static Integer getImageWidth() {
            if (imageWidth == null) {
                Font font = FontUtil.getDefaultSansSerifFont();
                int height = FontUtil.getFontMetrics(font).getHeight();
                imageWidth = height * 4;
            }
            return imageWidth;
        }

        String getText() {
            return getLabel().getText();
        }

        String getFeatureKey() {
            String ret = getText();
            int index = ret.indexOf("(");
            if (index > -1) {
                return ret.substring(0, index);
            } else {
                return ret;
            }
        }

        void setChecked(boolean selected) {
            getCheckbox().setSelected(selected);
        }

        private JCheckBox getCheckbox() {
            if (checkbox == null) {
                checkbox = new JCheckBox();
            }
            return checkbox;
        }
    }

    static class LinkPanel extends JPanel {

        private JLabel label;
        private JXHyperlink link;

        LinkPanel() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            GridBagConstraints c = null;

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            add(getLabel(), c);

            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 0;
            c.insets = new Insets(0, 3, 0, 0);
            add(getLink(), c);
        }

        private JLabel getLabel() {
            if (label == null) {
                label = new JLabel();
                final Font boldFont = label.getFont().deriveFont(Font.BOLD);
                label.setFont(boldFont);
            }
            return label;
        }

        public JXHyperlink getLink() {
            if (link == null) {
                link = new JXHyperlink();
            }
            return link;
        }

        public void setKey(String k) {
            getLabel().setText(k);
        }

        public void setValue(String v) {
            getLink().setText(v);
        }
    }
}

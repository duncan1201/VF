/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.common.ui.tooltip.JTTComponent;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.ColorCnst;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Qualifier;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author dunqiang
 */
public class ToolTipCreatorHelper {

    public static synchronized JComponent create(JTTComponent comp) {
        return create(comp, null);
    }
    
    public static synchronized JComponent create(JTTComponent comp, Integer totalPos) {

        JPanel richToolTip = (JPanel) comp.createRichToolTip();
        LayoutManager layout;
        layout = new GridBagLayout();
        richToolTip.setLayout(layout);

        Object obj = comp.getData();
        if (obj instanceof Feture) {
            Feture f = (Feture) obj;
            GridBagConstraints constraints = null;
            JLabel nameLabel;
            JComponent valueLabel;

            // name
            nameLabel = createLabel("Name:", true);
            valueLabel = createLabel(f.getDisplayName());
            constraints = addRow(richToolTip, nameLabel, valueLabel, constraints);

            // type
            nameLabel = createLabel("Type:", true);
            valueLabel = createLabel(f.getKey());
            constraints = addRow(richToolTip, nameLabel, valueLabel, constraints);

            // length
            nameLabel = createLabel("Length:", true);
            valueLabel = createLabel(f.getLucation().width(totalPos).toString());
            constraints = addRow(richToolTip, nameLabel, valueLabel, constraints);

            // location
            nameLabel = createLabel("Location:", true);
            valueLabel = createLabel(f.getLucation().toString());
            constraints = addRow(richToolTip, nameLabel, valueLabel, constraints);

            Iterator<Qualifier> itr = f.getQualifierSet().getSortedQualifiers().iterator();

            while (itr.hasNext()) {
                Qualifier qualifer = itr.next();
                String key = qualifer.getKey();
                String value = qualifer.getValue();

                nameLabel = createLabel(key + ":", true);
                JComponent link;
                link = createLabel(value, false, 60);

                constraints = addRow(richToolTip, nameLabel, link, constraints);
            }
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
        return richToolTip;
    }

    private static GridBagConstraints addRow(JPanel richToolTip, JComponent nameComp, JComponent valueComp, GridBagConstraints c) {
        if (c == null) {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(0, 3, 0, 0);
            richToolTip.add(nameComp, c);

            c.gridx = 1;
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            richToolTip.add(valueComp, c);
        } else {
            c.gridx = 0;
            c.gridy = c.gridy + 1;
            c.anchor = GridBagConstraints.WEST;
            richToolTip.add(nameComp, c);

            c.gridx = 1;
            c.anchor = GridBagConstraints.WEST;
            richToolTip.add(valueComp, c);
        }
        return c;
    }

    private static JLabel createLabel(String text) {
        return createLabel(text, false);
    }

    private static JComponent createLink(String text) {
        String[] splits = text.split(":");
        if (splits.length == 2) {
            URI uri = BioUtil.getNCBIURI(splits[0], splits[1]);
            if (uri != null) {
                JXHyperlink ret = new JXHyperlink();
                ret.setURI(uri);
                ret.setText(text);
                return ret;
            } else {
                return createLabel(text);
            }
        } else {
            return createLabel(text);
        }
    }

    private static JXHyperlink createHyperlink(String text, ActionListener listener) {
        JXHyperlink ret = new JXHyperlink();
        ret.setText(text);
        ret.addActionListener(listener);
        return ret;
    }

    private static JLabel createLabel(String text, boolean bold) {
        return createLabel(text, bold, Integer.MAX_VALUE);
    }

    private static JLabel createLabel(String text, boolean bold, Integer maxChars) {
        JLabel ret = new JLabel();
        ret.setBackground(ColorCnst.TOOLTIP_BG);

        if (maxChars != null && text.length() > maxChars) {
            text = StrUtil.trim(text, maxChars);
            text += "...";
        }
        ret.setText(text);
        if (bold) {
            Font font = ret.getFont().deriveFont(Font.BOLD);
            ret.setFont(font);
        }
        return ret;
    }
}

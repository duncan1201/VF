/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tooltip;

import com.gas.common.ui.util.ColorCnst;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

/**
 *
 * @author dunqiang
 */
public abstract class JTTComponent extends JComponent implements ITTContentProvider {

    protected JComponent richToolTip;
    private String toolTipHtml;
    private JButton closeBtn;
    private RichToolTipContainer toolTipContainer = new RichToolTipContainer();

    @Override
    public Container getToolTipContent() {
        toolTipContainer.getContentPane().add(getRichToolTip(), BorderLayout.CENTER);
        return toolTipContainer;
    }

    @Override
    public void setRichToolTip(JComponent t, boolean mouseEnter, boolean propertyChange) {
        if (t != null) {
            this.richToolTip = t;
            ToolTipMgr.sharedInstance().registerComponent(this, mouseEnter, propertyChange);
        }
    }

    @Override
    public JComponent getRichToolTip() {
        if (richToolTip == null || richToolTip.getComponentCount() == 0) {
            createRichToolTip();
        }
        return richToolTip;
    }

    @Override
    public JComponent createRichToolTip() {
        if (richToolTip == null || richToolTip.getComponentCount() == 0) {
            richToolTip = new JPanel();
            richToolTip.setOpaque(true);
            richToolTip.setBackground(ColorCnst.TOOLTIP_BG);
        }
        return richToolTip;
    }

    public JButton getCloseBtn() {
        return closeBtn;
    }
}

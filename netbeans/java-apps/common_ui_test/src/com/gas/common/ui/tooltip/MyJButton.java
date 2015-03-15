/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tooltip;

import com.gas.common.ui.painter.BackgroundPainter;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.ColorUtil;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;

/**
 *
 * @author dunqiang
 */
public class MyJButton extends JTTComponent {

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getSize().width, getSize().height);
    }

    @Override
    public JComponent getRichToolTip() {
        if (richToolTip == null) {
            createRichToolTip();
        }
        return richToolTip;
    }

    @Override
    public JComponent createRichToolTip() {
        richToolTip = super.createRichToolTip();
        
        JXButton btn = new JXButton("button 1");
        btn.setBackground(ColorCnst.TOOLTIP_BG);
        btn.setBackgroundPainter(new BackgroundPainter(ColorCnst.TOOLTIP_BG));
        richToolTip.add(btn);
        JXLabel bl = new JXLabel("label");
        bl.setBackgroundPainter(new BackgroundPainter(ColorCnst.TOOLTIP_BG));
        bl.setBackground(ColorCnst.TOOLTIP_BG);
        richToolTip.add(bl);
        return richToolTip;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tooltip;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.ColorCnst;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author dunqiang
 */
public class RichToolTipContainer extends JPanel {

    private JPanel contentPane;

    public RichToolTipContainer() {
        setBackground(ColorCnst.TOOLTIP_BG);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        contentPane = new JPanel(new BorderLayout());
        add(contentPane, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        FlatBtn close = new FlatBtn(ImageHelper.createImageIcon(ImageNames.CLOSE_16));
        close.setFocusable(false);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ToolTipMgr.sharedInstance().hideImmediately();
            }
        });
        add(close, c);
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}

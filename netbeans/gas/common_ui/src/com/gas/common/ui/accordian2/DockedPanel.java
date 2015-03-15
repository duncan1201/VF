/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

class DockedPanel extends JPanel {

    List<FlatBtn> btns = new ArrayList<FlatBtn>();

    DockedPanel() {
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        //add(flatBtn, c);
        add(new JLabel(), c);

        c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);
    }

    void addDecorator(JComponent comp) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        add(comp, c);
    }

    void addBar(Icon icon, final String text) {
        final FlatBtn btn = new FlatBtn(icon);
        btn.setActionCommand(text);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutlookPane pane = UIUtil.getParent(btn, OutlookPane.class);
                pane.show(e.getActionCommand());
            }
        });
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        add(btn, c);
        btns.add(btn);
    }

    void remove(FlatBtn comp) {
        super.remove(comp);
        btns.remove(comp);
    }

    List<FlatBtn> getDockedBtns() {
        return btns;
    }
}

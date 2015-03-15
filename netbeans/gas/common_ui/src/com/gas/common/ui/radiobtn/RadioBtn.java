/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.radiobtn;

import com.gas.common.ui.button.FlatBtn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author dq
 */
public class RadioBtn extends JPanel {

    private JRadioButton realBtn;
    private FlatBtn labelBtn;

    public RadioBtn() {
        this("");
    }

    public RadioBtn(String str) {
        this(str, null);
    }

    public RadioBtn(Icon icon) {
        this("", icon);
    }

    public RadioBtn(Icon icon, boolean selected) {
        this("", icon, selected);
    }

    public RadioBtn(String text, Icon icon) {
        this(text, icon, false);
    }

    public void setSelected(boolean selected) {
        realBtn.setSelected(selected);
        revalidate();
    }

    public boolean isSelected() {
        return realBtn.isSelected();
    }

    public RadioBtn(String text, Icon icon, boolean selected) {
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        realBtn = new JRadioButton("", selected);
        add(realBtn, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        if (icon != null) {
            labelBtn = new FlatBtn(icon, text, false);
        } else {
            labelBtn = new FlatBtn(text, false);
        }
        add(labelBtn, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        //add(filler, c);

        labelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = realBtn.isSelected();
                if (!selected) {
                    realBtn.setSelected(true);
                }
            }
        });
    }

    public JRadioButton getRealBtn() {
        return realBtn;
    }

    public void setText(String text) {
        labelBtn.setText(text);
        revalidate();
    }

    public void setIcon(Icon icon) {
        labelBtn.setIcon(icon);
    }

    public void addActionListener(ActionListener lis) {
        getRealBtn().addActionListener(lis);
    }

    public void addItemListener(ItemListener itemListener) {
        getRealBtn().addItemListener(itemListener);
    }
}
